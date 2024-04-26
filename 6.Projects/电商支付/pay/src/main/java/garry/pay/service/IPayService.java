package garry.pay.service;

import com.lly835.bestpay.model.PayResponse;
import garry.pay.pojo.PayInfo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Garry
 * ---------2024/3/6 10:03
 **/

@Service
public interface IPayService {

    /**
     * 创建支付
     *
     * @param orderId 订单号
     * @param amount  支付金额
     */
    PayResponse create(String orderId, BigDecimal amount);

    /**
     * @param StringNotifyData 异步通知的信息
     * @return 返回给微信服务端的信息（可以让它不要再发了）
     */
    String asyncNotify(String StringNotifyData);

    /**
     * 根据订单号查询支付详情
     *
     * @param orderId
     * @return
     */
    PayInfo queryByOrderId(String orderId);
}
