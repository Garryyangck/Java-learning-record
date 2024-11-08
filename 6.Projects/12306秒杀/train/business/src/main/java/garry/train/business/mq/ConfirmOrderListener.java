package garry.train.business.mq;

import com.alibaba.fastjson.JSON;
import garry.train.business.enums.ConfirmOrderStatusEnum;
import garry.train.business.form.ConfirmOrderDoForm;
import garry.train.business.pojo.ConfirmOrder;
import garry.train.business.service.ConfirmOrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @author Garry
 * 2024-11-08 14:48
 */
@Slf4j
@Service
@RocketMQMessageListener(consumerGroup = "default", topic = "CONFIRM_ORDER")
public class ConfirmOrderListener implements RocketMQListener<MessageExt> {

    @Resource
    private ConfirmOrderService confirmOrderService;

    @Override
    public void onMessage(MessageExt messageExt) {
        String formJson = new String(messageExt.getBody());
        log.info("接收到消息：{}", formJson);
        ConfirmOrderDoForm form = JSON.parseObject(formJson, ConfirmOrderDoForm.class);
        // 将订单状态改为 处理中
        ConfirmOrder confirmOrder = confirmOrderService.save(form, ConfirmOrderStatusEnum.PENDING);
        log.info("将订单状态修改为 PENDING：{}", confirmOrder);
        confirmOrderService.doConfirm(form);
    }
}
