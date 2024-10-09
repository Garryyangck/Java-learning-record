package garry.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import garry.train.business.enums.ConfirmOrderStatusEnum;
import garry.train.business.form.ConfirmOrderDoForm;
import garry.train.business.form.ConfirmOrderQueryForm;
import garry.train.business.form.ConfirmOrderTicketForm;
import garry.train.business.mapper.ConfirmOrderMapper;
import garry.train.business.pojo.ConfirmOrder;
import garry.train.business.pojo.ConfirmOrderExample;
import garry.train.business.pojo.DailyTrainTicket;
import garry.train.business.pojo.Train;
import garry.train.business.service.ConfirmOrderService;
import garry.train.business.service.DailyTrainTicketService;
import garry.train.business.service.TrainService;
import garry.train.business.vo.ConfirmOrderQueryVo;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.CommonUtil;
import garry.train.common.vo.PageVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Garry
 * 2024-10-07 19:53
 */
@Slf4j
@Service
public class ConfirmOrderServiceImpl implements ConfirmOrderService {
    @Resource
    private TrainService trainService;

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;

    @Override
    public void init(ConfirmOrderDoForm form) {
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(form, ConfirmOrder.class);
        DateTime now = DateTime.now();

        // 插入时要看数据库有没有唯一键约束，在此校验唯一键约束，防止出现 DuplicationKeyException

        // 对Id、createTime、updateTime 重新赋值
        // 可能还需要重新赋值其它的字段，比如 ConfirmOrder.memberId
        confirmOrder.setId(CommonUtil.getSnowflakeNextId());
        confirmOrder.setMemberId(form.getMemberId());
        confirmOrder.setCreateTime(now);
        confirmOrder.setUpdateTime(now);

        // 设置初始 status 以及 json 类型的 tickets
        confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
        confirmOrder.setTickets(JSONObject.toJSONString(form.getTickets()));

        confirmOrderMapper.insert(confirmOrder);
        log.info("插入初始化确认订单：{}", confirmOrder);
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
    public void doConfirm(ConfirmOrderDoForm form) {
        // 业务数据校验
        if (!checkConfirmOrder(form)) {
            throw new BusinessException(ResponseEnum.BUSINESS_CONFIRM_ORDER_CHECK_FAILED);
        }

        // 创建对象，插入 confirm_order 表，状态为初始
        init(form);

        // 选座 (当然，要先根据 tickets 判断用户是否要选座)

            // 遍历每一个车厢，首先找到 SeatType 符合的车厢

                // 按座位顺序 (index) 遍历车厢的座位，先找到第一个 SeatCol.code 正确的座位
                // 然后判断其 start ~ end 是否被卖出 (为了提高效率，把 sell 换为 int 类型，进行二进制数的位运算进行判断)
                // 没有卖出则判断是否选 > 1 个座位，如果是则寻找从当前座位开始的两排内，是否满足所有选座条件
                // 不满足则返回第一个正确座位的 index，继续向后遍历，直到找到新的正确座位，或者遍历完该车厢

        // 遍历完所有车厢也没有找到合适选座，或不选座，则自动分配座位 (同一车厢，顺序分配未卖出的座位)

        // 选完所有座位后 trx (事务) 处理

            // daily_train_seat 修改 sell 售卖情况
            // daily_train_ticket 修改余票数
            // (member)ticket 增加用户购票的记录
            // confirm_order 修改状态为成功
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
        List<Train> trains = trainService.queryByCode(form.getTrainCode());
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
            List<ConfirmOrderTicketForm> ticketList = JSON.parseObject(confirmOrder.getTickets(), new TypeReference<>() {});
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
}
