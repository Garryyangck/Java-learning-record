package garry.pay.controller;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.model.PayResponse;
import garry.pay.pojo.PayInfo;
import garry.pay.service.impl.PayServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Garry
 * ---------2024/3/6 11:57
 **/

@Controller//不要用RestController，我们要用mav渲染网页的，RestController只能返回对象
@RequestMapping("/pay")
public class PayController {
    @Resource
    private PayServiceImpl payService;

    @Resource
    private WxPayConfig wxPayConfig;

    /**
     * 创建支付，得到微信回复，根据code_url生成支付二维码
     *
     * @param orderId 订单号
     * @param amount  交易金额
     * @return 将codeUrl转发至create.ftl
     */
    @GetMapping({"/create"})
    public ModelAndView create(@RequestParam(value = "orderId") String orderId,
                               @RequestParam(value = "amount") String amount) {
        //payService创建订单，返回服务器的response，response.getCodeUrl()
        PayResponse response = payService.create(orderId, new BigDecimal("0.01"));

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId/*就是orderNo*/);
        map.put("codeUrl", response.getCodeUrl()/*生成二维码*/);
        map.put("returnUrl", wxPayConfig.getReturnUrl()/*支付成功跳转的地址*/);
        return new ModelAndView("create"/*不要加.ftl*/, map);
    }

    /**
     * 检验异步通知，用户支付成功后微信才会进行异步通知
     *
     * @param notifyData 异步通知信息
     * @return 告诉微信不要再通知了
     */
    @PostMapping({"/notify"})
    @ResponseBody//上面是Controller，不是RestController，因此要加ResponseBody
    public String asyncNotify(@RequestBody/*接收前端传递给后端的请求体中的数据*/
                                          String notifyData) {
        //检验异步通知
        return payService.asyncNotify(notifyData);
    }

    /**
     * 用于create页面ajax不断向此处发送请求，不断查看return的platformStatus是否为'支付成功'
     * @param orderId orderNo
     * @return PayInfo.platformStatus
     */
    @GetMapping("/queryByOrderId")
    @ResponseBody
    public PayInfo queryByOrderId(@RequestParam String orderId) {
        return payService.queryByOrderId(orderId);
    }
}
