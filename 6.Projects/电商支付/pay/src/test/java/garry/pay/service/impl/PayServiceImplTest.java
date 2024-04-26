package garry.pay.service.impl;

import garry.pay.PayApplicationTest;
import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author Garry
 * ---------2024/3/6 10:33
 **/
public class PayServiceImplTest extends PayApplicationTest {
    @Resource
    private PayServiceImpl payServiceImpl;

    @Resource
    private AmqpTemplate/*和JdbcTemplate类似*/ amqpTemplate;

    @Test
    public void create() {
        payServiceImpl.create("15654561231654", new BigDecimal("0.01"));
    }

    @Test
    public void sendMQMsg() {
        amqpTemplate.convertAndSend("payNotify"/*队列名*/,
                "hello rabbitMQ!"/*消息名*/);

    }
}