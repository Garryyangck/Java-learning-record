package garry.pay.service.impl;

import com.google.gson.Gson;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import garry.pay.dao.PayInfoMapper;
import garry.pay.enums.PayPlatformEnum;
import garry.pay.pojo.PayInfo;
import garry.pay.service.IPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author Garry
 * ---------2024/3/6 10:05
 **/

/**
 * 微信支付的实现类
 */
@Slf4j//日志
@Service
public class PayServiceImpl implements IPayService {
    private static final String QUEUE_PAY_NOTIFY = "payNotify";

    @Resource
    private BestPayService bestPayService;

    @Resource
    private PayInfoMapper payInfoMapper;

    @Resource
    private AmqpTemplate amqpTemplate;

    private final Gson gson = new Gson();

    /**
     * 根据传来的订单号和支付金额创建订单，并返回微信支付服务端返回的response
     * 将支付订单信息写入数据库
     *
     * @param orderId 订单号
     * @param amount  支付金额
     * @return 微信支付服务端返回的response
     */
    @Override
    public PayResponse create(String orderId/*就是orderNo*/, BigDecimal amount) {
        //1.写入数据库
        PayInfo payInfo = new PayInfo(Long.parseLong(orderId),
                PayPlatformEnum.WX.getCode(),
                OrderStatusEnum.NOTPAY.getDesc(),
                amount);
        //不能用insert，它会默认把你没赋值的time赋值为null
        payInfoMapper.insertSelective(payInfo);

        //2.设置PayRequest对象
        PayRequest payRequest = new PayRequest();
        payRequest.setOrderName("微信支付订单");
        payRequest.setOrderId(orderId);
        payRequest.setOrderAmount(amount.doubleValue());
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_NATIVE);

        //3.发起支付，接收返回
        PayResponse response = bestPayService.pay(payRequest);

        log.info("发起支付时微信返回的通知：{}", response);

        return response;
    }

    /**
     * 签名检验
     * 金额检验
     * 修改支付状态
     * 告诉微信不要再通知了
     *
     * @param notifyData 通知信息
     */
    @Override
    public String asyncNotify(String notifyData) {
        //1.获取String型异步通知中的信息payResponse
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("用户完成支付后微信返回的异步通知：{}", payResponse);

        //2.金额检验，通过orderNo从数据库中查询，检验交易金额
        PayInfo payInfo = payInfoMapper.selectByOrderNo(Long.valueOf(payResponse.getOrderId()));
        if (payInfo == null) {
            throw new RuntimeException("通过order_no=" + payResponse.getOrderId() + "查询到的结果是null");
        }
        //查询到的状态不是已支付
        if (!payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.getDesc())) {
            //比较金额，如果不相同
            if (payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount())) != 0) {
                throw new RuntimeException("异步通知金额和数据库金额不一致，orderNo=" + payResponse.getOrderId());
            }
            //3.修改订单支付状态，使用updateSelective
            payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.getDesc());
            payInfo.setPlatformNumber(payResponse.getOutTradeNo());//写入交易流水号(唯一索引)
            payInfo.setUpdateTime(null);//避免把最开始的updateTime又写入数据库，从而使Mysql自带的更新时间功能失效
            payInfoMapper.updateByPrimaryKeySelective(payInfo);
        }

        //pay发送MQ消息(payInfo)，mall接收MQ消息
        amqpTemplate.convertAndSend(QUEUE_PAY_NOTIFY, gson.toJson(payInfo));

        //4.告诉微信不要再通知了
        return "<xml>\n" +
                "   <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "   <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";
    }

    @Override
    public PayInfo queryByOrderId(String orderId) {
        return payInfoMapper.selectByOrderNo(Long.valueOf(orderId));
    }

}
