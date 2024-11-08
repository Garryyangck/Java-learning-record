package garry.train.business.mq;

import com.alibaba.fastjson.JSON;
import garry.train.business.form.ConfirmOrderDoForm;
import garry.train.business.service.ConfirmOrderService;
import garry.train.common.consts.CommonConst;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.MDC;
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
        ConfirmOrderDoForm form = JSON.parseObject(formJson, ConfirmOrderDoForm.class);
        MDC.put(CommonConst.LOG_ID, form.getLOG_ID());
        log.info("接收到消息：{}", formJson);
        confirmOrderService.doConfirm(form);
    }
}
