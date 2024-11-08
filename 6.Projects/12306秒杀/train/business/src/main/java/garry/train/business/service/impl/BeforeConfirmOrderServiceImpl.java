package garry.train.business.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import garry.train.business.enums.ConfirmOrderStatusEnum;
import garry.train.business.form.ConfirmOrderDoForm;
import garry.train.business.form.ConfirmOrderTicketForm;
import garry.train.business.pojo.ConfirmOrder;
import garry.train.business.pojo.DailyTrain;
import garry.train.business.pojo.DailyTrainTicket;
import garry.train.business.service.*;
import garry.train.common.consts.MQConst;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.util.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Garry
 * 2024-11-08 14:26
 */
@Service
@Slf4j
public class BeforeConfirmOrderServiceImpl implements BeforeConfirmOrderService {
    @Resource
    private DailyTrainService dailyTrainService;

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @Resource
    private ConfirmOrderService confirmOrderService;

    @Resource
    private SkTokenService skTokenService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Override
    @SentinelResource(value = "doConfirm", blockHandler = "doConfirmBlock")
    // 直接从 doConfirm COPY 过来的，懒得改 Nacos 配置，因此没有改 Resource 的名字
    public boolean beforeDoConfirm(ConfirmOrderDoForm form, Long memberId) {
        // 验证图片验证码，防止机器人刷票，以及打散订单提交的时间
        String redisKey = RedisUtil.getRedisKey4Kaptcha(form.getImageCodeToken());
        String rightImageCode = (String) redisTemplate.opsForValue().get(redisKey);
        if (StrUtil.isBlank(rightImageCode) || !rightImageCode.equalsIgnoreCase(form.getImageCode().trim())) {
            throw new BusinessException(ResponseEnum.BUSINESS_KAPTCHA_WRONG_IMAGE_CODE);
        }
        redisTemplate.delete(redisKey);

        // 在执行后面哪些更慢的 SQL 之前，先查令牌数，因为令牌查起来更快，如果令牌没有了，就可以认为库存没有了，当然如果还有令牌可以手动添加
        boolean validSkToken = skTokenService.validSkToken(form.getDate(), form.getTrainCode(), memberId);
        if (!validSkToken) {
            throw new BusinessException(ResponseEnum.BUSINESS_SK_TOKEN_GET_FAILED);
        }

        // 车次是否存在
        List<DailyTrain> trains = dailyTrainService.queryByDateAndCode(form.getDate(), form.getTrainCode());
        if (CollUtil.isEmpty(trains)) {
            log.info("Date = {} TrainCode = {} 下的车次不存在", form.getDate(), form.getTrainCode());
            return false;
        }

        // 车票是否存在
        List<DailyTrainTicket> dailyTrainTickets = dailyTrainTicketService.queryByDateAndTrainCodeAndStartAndEnd(form.getDate(), form.getTrainCode(), form.getStart(), form.getEnd());
        if (CollUtil.isEmpty(dailyTrainTickets)) {
            log.info("Date = {} TrainCode = {} 下的车票不存在", form.getDate(), form.getTrainCode());
            return false;
        }

        // 车次时间是否合法
//        if (System.currentTimeMillis() > form.getDate().getTime()) {
//            log.info("Date = {} TrainCode = {} 下的车次时间不合法", form.getDate(), form.getTrainCode());
//            return false;
//        }

        // 订单是否存在，即 tickets 是否 > 0
        if (CollUtil.isEmpty(form.getTickets())) {
            log.info("用户 {} 提交的订单为空", form.getMemberId());
            return false;
        }

        // 是否有余票
        DailyTrainTicket dailyTrainTicket = dailyTrainTickets.get(0);
        Map<String, Integer> seatCodeRemainNumMap = DailyTrainTicketService.getSeatCodeRemainNumMap(dailyTrainTicket);
        for (ConfirmOrderTicketForm ticketForm : form.getTickets()) {
            String seatTypeCode = ticketForm.getSeatTypeCode();
            if (seatCodeRemainNumMap.get(seatTypeCode) <= 0) {
                log.info("Date = {} TrainCode = {} 下无余票，seatTypeCode = {}", form.getDate(), form.getTrainCode(), seatTypeCode);
                throw new BusinessException(ResponseEnum.BUSINESS_CONFIRM_ORDER_LACK_OF_TICKETS);
            }
        }

        // 同一乘客不能购买同一车次
//        Set<Long> passengerIdSet = form.getTickets().stream()
//                .map(ConfirmOrderTicketForm::getPassengerId).collect(Collectors.toSet());
//        List<ConfirmOrder> confirmOrders = confirmOrderService.queryByMemberIdAndDateAndTrainCodeAndStartAndEnd(form.getMemberId(), form.getDate(), form.getTrainCode(), form.getStart(), form.getEnd());
//        for (ConfirmOrder confirmOrder : confirmOrders) {
//            // 使用 fastjson，将 JSON 字符串转化为 List<ConfirmOrderTicketForm> 对象
//            List<ConfirmOrderTicketForm> ticketForms = JSON.parseArray(confirmOrder.getTickets(), ConfirmOrderTicketForm.class);
//            for (ConfirmOrderTicketForm ticketForm : ticketForms) {
//                if (passengerIdSet.contains(ticketForm.getPassengerId())) {
//                    log.info("同一乘客 {} 不能购买同一车次 Date = {} TrainCode = {}", ticketForm.getPassengerName(), form.getDate(), form.getTrainCode());
//                    throw new BusinessException(ResponseEnum.BUSINESS_CONFIRM_ORDER_DUPLICATE_PASSENGER);
//                }
//            }
//        }

        log.info("订单校验成功");

        // 创建对象，插入 confirm_order 表，状态为初始
        ConfirmOrder confirmOrder = confirmOrderService.save(form, ConfirmOrderStatusEnum.INIT);
        log.info("插入 INIT 订单：{}", confirmOrder);
        form.setId(confirmOrder.getId());

        // 向 rocketmq 发送消息
        String formJSON = JSON.toJSONString(form);
        log.info("排队购票，发送 MQ 消息： {}", formJSON);
        rocketMQTemplate.convertAndSend(MQConst.CONFIRM_ORDER, formJSON);
        log.info("排队购票，发送 MQ 完成");

        return true;
    }

    /**
     * doConfirm 的降级方法
     */
    public boolean doConfirmBlock(ConfirmOrderDoForm form, Long memberId, BlockException e) {
        log.info("用户 {} 的 beforeDoConfirm 请求被降级处理", memberId);
        throw new BusinessException(ResponseEnum.BUSINESS_CONFIRM_ORDER_SENTINEL_BLOCKED);
    }
}
