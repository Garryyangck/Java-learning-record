package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.business.enums.ConfirmOrderStatusEnum;
import garry.train.business.enums.SeatColEnum;
import garry.train.business.form.ConfirmOrderDoForm;
import garry.train.business.form.ConfirmOrderQueryForm;
import garry.train.business.form.ConfirmOrderTicketForm;
import garry.train.business.mapper.ConfirmOrderMapper;
import garry.train.business.pojo.*;
import garry.train.business.service.*;
import garry.train.business.util.SellUtil;
import garry.train.business.vo.ConfirmOrderQueryVo;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    private ConfirmOrderMapper confirmOrderMapper;

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

    @Override
    @Async
    public void doConfirm(ConfirmOrderDoForm form) {
        // 业务数据校验
        if (!checkConfirmOrder(form)) {
            throw new BusinessException(ResponseEnum.BUSINESS_CONFIRM_ORDER_CHECK_FAILED);
        }

        // 创建对象，插入 confirm_order 表，状态为初始
        ConfirmOrder confirmOrder = save(form, ConfirmOrderStatusEnum.INIT);
        log.info("插入 INIT 订单：{}", confirmOrder);

        // 选座
        List<SeatChosen> seatChosenList = chooseSeat(form);

        // 模拟选座用时很久，毕竟还要排队什么的
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 选座成功后，进行相关座位售卖情况、余票数、购票信息、订单状态的修改，创建并通过 websocket 发送 message，事务处理
        if (!afterConfirmOrderService.afterDoConfirm(seatChosenList, form)) {
            throw new BusinessException(ResponseEnum.BUSINESS_CONFIRM_ORDER_HANDLE_SEAT_CHOSEN_FAILED);
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
     * 业务数据校验，车次是否存在，车票是否存在，车次时间是否合法，
     * tickets 是否 > 0，是否有余票，同一乘客不能购买同一车次
     */
    private boolean checkConfirmOrder(ConfirmOrderDoForm form) {
        // 车次是否存在
        List<DailyTrain> trains = dailyTrainService.queryByDateAndCode(form.getDate(), form.getTrainCode());
        if (CollUtil.isEmpty(trains)) {
            log.info("车次不存在");
            return false;
        }

        // 车票是否存在
        List<DailyTrainTicket> dailyTrainTickets = dailyTrainTicketService.queryByDateAndTrainCodeAndStartAndEnd(form.getDate(), form.getTrainCode(), form.getStart(), form.getEnd());
        if (CollUtil.isEmpty(dailyTrainTickets)) {
            log.info("车票不存在");
            return false;
        }

        // 车次时间是否合法
        if (System.currentTimeMillis() > form.getDate().getTime()) {
            log.info("车次时间不合法");
            return false;
        }

        // tickets 是否 > 0
        if (CollUtil.isEmpty(form.getTickets())) {
            log.info("tickets <= 0");
            return false;
        }

        // 是否有余票
        DailyTrainTicket dailyTrainTicket = dailyTrainTickets.get(0);
        Map<String, Integer> seatCodeRemainNumMap = DailyTrainTicketService.getSeatCodeRemainNumMap(dailyTrainTicket);
        for (ConfirmOrderTicketForm ticketForm : form.getTickets()) {
            String seatTypeCode = ticketForm.getSeatTypeCode();
            if (seatCodeRemainNumMap.get(seatTypeCode) <= 0) {
                log.info("无余票，seatTypeCode = {}", seatTypeCode);
                throw new BusinessException(ResponseEnum.BUSINESS_CONFIRM_ORDER_LACK_OF_TICKETS);
            }
        }

        // 同一乘客不能购买同一车次
        Set<Long> passengerIdSet = form.getTickets().stream()
                .map(ConfirmOrderTicketForm::getPassengerId).collect(Collectors.toSet());
        List<ConfirmOrder> confirmOrders = queryByMemberIdAndDateAndTrainCodeAndStartAndEnd(form.getMemberId(), form.getDate(), form.getTrainCode(), form.getStart(), form.getEnd());
        for (ConfirmOrder confirmOrder : confirmOrders) {
            // 使用 fastjson，将 JSON 字符串转化为 List<ConfirmOrderTicketForm> 对象
            List<ConfirmOrderTicketForm> ticketList = JSON.parseObject(confirmOrder.getTickets(), new TypeReference<>() {
            });
            for (ConfirmOrderTicketForm ticketForm : ticketList) {
                if (passengerIdSet.contains(ticketForm.getPassengerId())) {
                    log.info("同一乘客不能购买同一车次");
                    throw new BusinessException(ResponseEnum.BUSINESS_CONFIRM_ORDER_DUPLICATE_PASSENGER);
                }
            }
        }

        log.info("订单校验成功");
        return true;
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
