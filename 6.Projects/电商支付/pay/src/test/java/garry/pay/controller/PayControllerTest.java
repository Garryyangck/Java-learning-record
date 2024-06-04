package garry.pay.controller;

import garry.pay.PayApplicationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;

import javax.annotation.Resource;

/**
 * @author Garry
 * ---------2024/6/4 16:55
 **/
@Slf4j
public class PayControllerTest extends PayApplicationTest {
    @Resource
    private AmqpTemplate amqpTemplate;

    @Test
    public void sendMQMsg() {
        amqpTemplate.convertAndSend("payNotify", "hello");
    }
}