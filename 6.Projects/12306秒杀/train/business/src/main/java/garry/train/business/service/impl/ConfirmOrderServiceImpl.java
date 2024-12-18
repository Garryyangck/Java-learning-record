package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.business.enums.ConfirmOrderStatusEnum;
import garry.train.business.enums.SeatColEnum;
import garry.train.business.form.ConfirmOrderDoForm;
import garry.train.business.form.ConfirmOrderQueryForm;
import garry.train.business.form.ConfirmOrderTicketForm;
import garry.train.business.mapper.ConfirmOrderMapper;
import garry.train.business.pojo.ConfirmOrder;
import garry.train.business.pojo.ConfirmOrderExample;
import garry.train.business.pojo.DailyTrainCarriage;
import garry.train.business.pojo.DailyTrainSeat;
import garry.train.business.service.*;
import garry.train.business.util.SellUtil;
import garry.train.business.vo.ConfirmOrderQueryVo;
import garry.train.common.consts.CommonConst;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.CommonUtil;
import garry.train.common.util.RedisUtil;
import garry.train.common.vo.PageVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.MDC;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Garry
 * 2024-10-07 19:53
 */
@Slf4j
@Service
public class ConfirmOrderServiceImpl implements ConfirmOrderService {
    @Resource
    private DailyTrainService dailyTrainService;

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @Resource
    private AfterConfirmOrderService afterConfirmOrderService;

    @Resource
    private MessageService messageService;

    @Resource
    private SkTokenService skTokenService;

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public ConfirmOrder save(ConfirmOrderDoForm form, ConfirmOrderStatusEnum statusEnum) {
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(form, ConfirmOrder.class);
        DateTime now = DateTime.now();

        // 设置初始 status 以及 json 类型的 tickets
        confirmOrder.setTickets(JSONObject.toJSONString(form.getTickets()));
        confirmOrder.setStatus(statusEnum.getCode());
        confirmOrder.setUpdateTime(now);

        // 判断是新增还是修改
        if (statusEnum.equals(ConfirmOrderStatusEnum.INIT)) {
            // 插入时要看数据库有没有唯一键约束，在此校验唯一键约束，防止出现 DuplicationKeyException

            // 为 id 和 createTime 赋值
            confirmOrder.setId(CommonUtil.getSnowflakeNextId());
            confirmOrder.setCreateTime(now);
            confirmOrderMapper.insert(confirmOrder);
        } else {
            confirmOrderMapper.updateByPrimaryKeySelective(confirmOrder);
        }
        return confirmOrder;
    }

    @Override
    public PageVo<ConfirmOrderQueryVo> queryList(ConfirmOrderQueryForm form) {
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        confirmOrderExample.setOrderByClause("date, update_time desc");
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();
        // 这里自定义一些过滤的条件，比如:
//        // 用户只能查自己 memberId 下的确认订单
//        if (ObjectUtil.isNotNull()) {
//            criteria.andMemberIdEqualTo(memberId);
//        }

        // 启动分页
        PageHelper.startPage(form.getPageNum(), form.getPageSize());

        // 获取 confirmOrders
        List<ConfirmOrder> confirmOrders = confirmOrderMapper.selectByExample(confirmOrderExample);

        // 获得 pageInfo 对象，并将其 List 的模板类型改为 ConfirmOrderQueryVo
        // 注意这里必须先获取 pageInfo，再尝试获取 List<ConfirmOrderQueryVo>，否则无法正确获取 pageNum，pages 等重要属性
        PageInfo<ConfirmOrder> pageInfo = new PageInfo<>(confirmOrders);
        List<ConfirmOrderQueryVo> voList = BeanUtil.copyToList(pageInfo.getList(), ConfirmOrderQueryVo.class);

        // 获取 PageVo 对象
        PageVo<ConfirmOrderQueryVo> vo = BeanUtil.copyProperties(pageInfo, PageVo.class);
        vo.setList(voList);
        vo.setMsg("查询确认订单列表成功");
        return vo;
    }

    @Override
    public void delete(Long id) {
        confirmOrderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 由于此方法异步执行，执行时 Controller 已经给浏览器返回结果了，
     * 因此此方法抛出的异常，不会被统一异常处理，因为统一异常处理的返回是给浏览器，但是此刻浏览器已经有返回值了
     */
    @Override
    @Async
    public void doConfirm(ConfirmOrderDoForm form) {
        // 由于这是一个异步的方法，Controller 是一个线程，该方法是另一个线程，不共享 Controller 的 LOG_ID，这就是该方法的日志没有流水号的原因
        // 因此可以让 Controller 把自己的 LOG_ID 传过来
        MDC.put(CommonConst.LOG_ID, form.getLOG_ID());

        // 获取分布式锁，因为同一时刻，只能有一个线程执行 (从队列中取订单 + 出票) 的逻辑
        String redisKey = RedisUtil.getRedisKey4DailyTicketDistributedLock(form.getDate(), form.getTrainCode());
        RLock rLock = null;
        try {
            // 使用 redisson，通过“看门狗”机制，redisKey 无限过期时间，实现安全的 redis 分布式锁
            rLock = redissonClient.getLock(redisKey);
            // boolean tryLock = rLock.tryLock(30/* wait_time，如果没有拿到锁，则阻塞 30 秒 */, 30/* 分布式锁过期时间 */, TimeUnit.SECONDS); // 不带看门狗，阻塞 30 秒等待，分布式锁 5 秒自动释放，有并发风险
            boolean tryLock = rLock.tryLock(0/* wait_time，如果没有拿到锁，则阻塞 0 秒 */, TimeUnit.SECONDS); // 带看门狗，无限刷新 EXPIRE_TIME，不会引起多个线程进来造成的并发风险
            if (tryLock) {
                log.info("成功抢到锁 {}", redisKey);

                while (true) {
                    // 不管 form 是谁的订单，我直接从数据库里查订单，因为数据库中的订单都抢到了 SKToken，因此都需要出票
                    ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
                    confirmOrderExample.setOrderByClause("id");
                    confirmOrderExample.createCriteria()
                            .andDateEqualTo(form.getDate())
                            .andTrainCodeEqualTo(form.getTrainCode())
                            .andStatusEqualTo(ConfirmOrderStatusEnum.INIT.getCode());
                    PageHelper.startPage(1, CommonConst.CONFIRM_ORDER_PER_HANDLE); // 一次只查 5 条进行处理，防止一次性太多订单，内存装不下
                    List<ConfirmOrder> confirmOrders = confirmOrderMapper.selectByExample(confirmOrderExample);

                    // 数据库中该 Date TrainCode 下的订单全部查完了，break，完成这一次出票
                    if (CollUtil.isEmpty(confirmOrders)) {
                        log.info("分布式锁 {} 下的 INIT 订单已全部处理完毕", redisKey);
                        break;
                    }

                    // 一个一个订单地出票
                    for (ConfirmOrder confirmOrder : confirmOrders) {
                        ConfirmOrderDoForm confirmOrderDoForm = new ConfirmOrderDoForm();

                        try {
                            log.info("分布式锁 {} 下正在处理的订单：{}", redisKey, confirmOrder);
                            confirmOrderDoForm.setId(confirmOrder.getId());
                            confirmOrderDoForm.setMemberId(confirmOrder.getMemberId());
                            confirmOrderDoForm.setDate(confirmOrder.getDate());
                            confirmOrderDoForm.setTrainCode(confirmOrder.getTrainCode());
                            confirmOrderDoForm.setStart(confirmOrder.getStart());
                            confirmOrderDoForm.setEnd(confirmOrder.getEnd());
                            confirmOrderDoForm.setDailyTrainTicketId(confirmOrder.getDailyTrainTicketId());
                            // 不能使用 copyProperties，因为 list 可以转为 String，但是 String 无法转为 list，无法完成 tickets 字段的转换，会报错
                            confirmOrderDoForm.setTickets(JSONObject.parseArray(confirmOrder.getTickets(), ConfirmOrderTicketForm.class));
                            confirmOrderDoForm.setLOG_ID(form.getLOG_ID());
                            // confirm_order 修改状态为 处理中
                            confirmOrder = save(confirmOrderDoForm, ConfirmOrderStatusEnum.PENDING);
                            log.info("将订单状态修改为 PENDING：{}", confirmOrder);
                            // 选座
                            List<SeatChosen> seatChosenList = chooseSeat(confirmOrderDoForm);
                            // 选座成功后，进行相关座位售卖情况、余票数、购票信息、订单状态的修改，创建并通过 websocket 发送 message，事务处理
                            afterConfirmOrderService.afterDoConfirm(seatChosenList, confirmOrderDoForm);

                        } catch (Exception e) {
                            if (e instanceof BusinessException
                                    && ResponseEnum.BUSINESS_CONFIRM_ORDER_CHOOSE_SEAT_FAILED
                                    .equals(((BusinessException) e).getResponseEnum())) {
                                // 将订单状态改为 无票
                                confirmOrder = save(confirmOrderDoForm, ConfirmOrderStatusEnum.EMPTY);
                                log.info("将订单状态修改为 EMPTY：{}", confirmOrder);
                            } else {
                                // 将订单状态改为 失败
                                confirmOrder = save(confirmOrderDoForm, ConfirmOrderStatusEnum.FAILURE);
                                log.info("将订单状态修改为 FAILURE：{}", confirmOrder);
                            }
                            // 发送消息，没能选到座位
                            sendFailMessage(confirmOrderDoForm);
                        }
                    }
                }

            } else {
                log.info("未能抢到锁 {}，其它线程正在处理该分布式锁下的订单", redisKey);
                // 没能抢到锁的线程，直接返回也无所谓，因为证明有其它线程拿到锁，会执行 (从队列中取订单 + 出票) 的逻辑
                return;
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);

        } finally {
            log.info("执行完毕，释放锁 {}", redisKey);
            if (null != rLock && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    @Override
    public List<ConfirmOrder> queryByMemberIdAndDateAndTrainCodeAndStartAndEnd(Long memberId, Date date, String trainCode, String start, String end) {
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        confirmOrderExample.setOrderByClause("date, update_time desc");
        confirmOrderExample.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode)
                .andStartEqualTo(start)
                .andEndEqualTo(end);
        return confirmOrderMapper.selectByExample(confirmOrderExample);
    }

    /**
     * 选座，全为一等座或二等座时会根据相对位置选座，选座必须在同一车厢
     * 如果不能选座或选座失败，则自动分配符合 seatType 的座位
     */
    private List<SeatChosen> chooseSeat(ConfirmOrderDoForm form) {
        ArrayList<SeatChosen> seatChosenList = new ArrayList<>();
        Integer startIndex = dailyTrainStationService.queryByDateAndTrainCodeAndName(form.getDate(), form.getTrainCode(), form.getStart()).get(0).getIndex();
        Integer endIndex = dailyTrainStationService.queryByDateAndTrainCodeAndName(form.getDate(), form.getTrainCode(), form.getEnd()).get(0).getIndex();
        List<DailyTrainCarriage> trainCarriages = dailyTrainCarriageService.queryByDateAndTrainCode(form.getDate(), form.getTrainCode());
        List<DailyTrainSeat> trainSeats = dailyTrainSeatService.queryByDateAndTrainCode(form.getDate(), form.getTrainCode());
        ConfirmOrderTicketForm firstTicketForm = form.getTickets().get(0);

        // 先根据 ticket.seat 判断用户是否要选座
        if (StrUtil.isNotBlank(firstTicketForm.getSeat())) {
            String firstSeat = firstTicketForm.getSeat(); // "A1"
            String firstSeatCol = firstSeat.substring(0, 1); // "A"
            int firstSeatRow = Integer.parseInt(firstSeat.substring(1)); // 1 相对排数
            String seatTypeCode = firstTicketForm.getSeatTypeCode(); // "1" 一等座
            log.info("用户要选座，seatTypeCode = {}", seatTypeCode);

            // 遍历每一个车厢，首先找到 SeatType 符合的车厢
            for (DailyTrainCarriage trainCarriage : trainCarriages) {
                if (!seatTypeCode.equals(trainCarriage.getSeatType())) {
                    continue;
                }

                List<String> colTypes = SeatColEnum.getColsByType(seatTypeCode).stream().map(SeatColEnum::getCode).toList(); // ["A", "C", "D", "F"]
                List<DailyTrainSeat> carriageSeats = trainSeats.stream()
                        .filter(trainSeat -> trainSeat.getCarriageIndex().equals(trainCarriage.getIndex()))
                        .sorted(Comparator.comparingInt(DailyTrainSeat::getCarriageSeatIndex)).toList();

                // 按座位顺序 (carriageSeatIndex) 遍历车厢的座位
                for (int carriageSeatIndex = 1; carriageSeatIndex <= carriageSeats.size(); carriageSeatIndex++) {
                    int row = (carriageSeatIndex - 1) / colTypes.size() + 1;
                    int col = carriageSeatIndex - (row - 1) * colTypes.size();
                    String colType = colTypes.get(col - 1); // 注意是 col - 1
                    int finalCarriageSeatIndex = carriageSeatIndex;
                    DailyTrainSeat seat = carriageSeats.stream()
                            .filter(carriageSeat -> carriageSeat.getCarriageSeatIndex().equals(finalCarriageSeatIndex))
                            .toList().get(0); // 第 carriageSeatIndex 位置的 dailySeat

                    // 先找到第一个 SeatCol.code 正确的座位，并判断其 start ~ end 是否被卖出
                    if (firstSeatCol.equals(colType)
                            && !SellUtil.isSold(seat.getSell(), startIndex, endIndex)) {
                        ArrayList<Integer> chosenSeatsIndex = new ArrayList<>();
                        chosenSeatsIndex.add(carriageSeatIndex);
                        int index = carriageSeatIndex; // 继续遍历的 index

                        // 遍历不属于第一个乘客的 ticket
                        for (int i = 1; i < form.getTickets().size(); i++) {
                            String passengerSeat = form.getTickets().get(i).getSeat(); // 非第一个乘客的 ticket.seat "C2"
                            String seatCol = passengerSeat.substring(0, 1); // "C"
                            int seatRow = Integer.parseInt(passengerSeat.substring(1)); // 2
                            int carriageSeatIndexLimit = Math.min((row + (seatRow - firstSeatRow)) * colTypes.size(), trainCarriage.getSeatCount()); // 该乘客可选座位的 carriageIndex 上限

                            // 遍历该乘客可选的座位
                            for (index = index + 1; index <= carriageSeatIndexLimit; index++) {
                                int _row = (index - 1) / colTypes.size() + 1;
                                int _col = index - (_row - 1) * colTypes.size();
                                String _colType = colTypes.get(_col - 1);  // 注意是 _col - 1
                                int finalIndex = index;
                                DailyTrainSeat _seat = carriageSeats.stream()
                                        .filter(carriageSeat -> carriageSeat.getCarriageSeatIndex().equals(finalIndex))
                                        .toList().get(0); // 第 index 位置的 dailySeat

                                // 判断其 seatCol，和 row 的相对位置，以及是否卖出
                                if (seatCol.equals(_colType)
                                        && ((_row - row) == (seatRow - firstSeatRow))
                                        && !SellUtil.isSold(_seat.getSell(), startIndex, endIndex)) {
                                    chosenSeatsIndex.add(index);
                                    break;
                                }
                            }
                        }

                        // 遍历完其它所有乘客，检查 chosenSeatsIndex 选中的个数是否与 form.getTickets().size() 相等，相等则成功
                        if (chosenSeatsIndex.size() == form.getTickets().size()) {
                            // 构造 SeatChosen，return
                            for (int i = 0; i < chosenSeatsIndex.size(); i++) {
                                Integer seatsIndex = chosenSeatsIndex.get(i);
                                DailyTrainSeat dailyTrainSeat = carriageSeats.stream()
                                        .filter(carriageSeat -> carriageSeat.getCarriageSeatIndex().equals(seatsIndex))
                                        .toList().get(0);
                                SeatChosen seatChosen = getSeatChosen(form, i, dailyTrainSeat);
                                seatChosenList.add(seatChosen);
                            }

                            log.info("用户选座成功，seatChosenList = {}", seatChosenList);
                            return seatChosenList;
                        }
                    }
                }
            }
        }

        // 遍历完所有车厢也没有找到合适选座，或不选座，则自动分配座位 (从第一个车厢开始，分配遍历到的第一个没卖出的座位)
        log.info("用户不选座，或选座失败");
        ArrayList<Integer> chosenSeatsIndex = new ArrayList<>();
        List<DailyTrainSeat> seatList = trainSeats.stream()
                .filter(trainSeat -> !SellUtil.isSold(trainSeat.getSell(), startIndex, endIndex))
                .sorted((o1, o2) -> {
                    if (!Objects.equals(o1.getCarriageIndex(), o2.getCarriageIndex())) {
                        return o1.getCarriageIndex() - o2.getCarriageIndex();
                    } else {
                        return o1.getCarriageSeatIndex() - o2.getCarriageSeatIndex();
                    }
                }).toList(); // 没被卖出的座位

        // 遍历每一个座位
        for (int index = 0; index < seatList.size(); index++) {
            DailyTrainSeat seat = seatList.get(index);

            // 遍历每一个乘客
            for (int ticketIndex = 0; ticketIndex < form.getTickets().size(); ticketIndex++) {
                // 当前乘客已选座位，则 continue
                if (chosenSeatsIndex.size() > ticketIndex) {
                    continue;
                }

                String seatTypeCode = form.getTickets().get(ticketIndex).getSeatTypeCode();
                if (seatTypeCode.equals(seat.getSeatType())) {
                    chosenSeatsIndex.add(index);
                    break;
                }
            }

            // 如果已为每一个乘客选好座位，则直接 break
            if (chosenSeatsIndex.size() == form.getTickets().size()) {
                break;
            }
        }

        // 遍历完所有乘客，检查 chosenSeatsIndex 选中的个数是否与 form.getTickets().size() 相等，相等则成功
        if (chosenSeatsIndex.size() == form.getTickets().size()) {
            // 构造 SeatChosen，return
            for (int i = 0; i < chosenSeatsIndex.size(); i++) {
                Integer index = chosenSeatsIndex.get(i);
                DailyTrainSeat dailyTrainSeat = seatList.get(index);
                SeatChosen seatChosen = getSeatChosen(form, i, dailyTrainSeat);
                seatChosenList.add(seatChosen);
            }

        } else {
            throw new BusinessException(ResponseEnum.BUSINESS_CONFIRM_ORDER_CHOOSE_SEAT_FAILED);
        }

        log.info("系统自动分配的座位 seatChosenList = {}", seatChosenList);
        return seatChosenList;
    }

    private void sendFailMessage(ConfirmOrderDoForm form) {
        // 发送消息，没能选到座位
        String content = "很抱歉，您购买的【%s】从【%s】开往【%s】的【%s】车次的车票购买失败。"
                .formatted(DateUtil.format(form.getDate(), "yyyy-MM-dd"),
                        form.getStart(), form.getEnd(), form.getTrainCode());
        messageService.sendSystemMessage(form.getMemberId(), content);
        log.info("发送选座失败消息：{}", content);
    }

    /**
     * @param i tickets 的顺序，也是遍历 chosenSeatsIndex 的 i
     */
    private SeatChosen getSeatChosen(ConfirmOrderDoForm form, int i, DailyTrainSeat dailyTrainSeat) {
        SeatChosen seatChosen = BeanUtil.copyProperties(form, SeatChosen.class);
        seatChosen.setTicket(form.getTickets().get(i));
        seatChosen.setCarriageIndex(dailyTrainSeat.getCarriageIndex());
        seatChosen.setRow(dailyTrainSeat.getRow());
        seatChosen.setCol(dailyTrainSeat.getCol());
        seatChosen.setSeatType(dailyTrainSeat.getSeatType());
        seatChosen.setCarriageSeatIndex(dailyTrainSeat.getCarriageSeatIndex());
        return seatChosen;
    }
}
