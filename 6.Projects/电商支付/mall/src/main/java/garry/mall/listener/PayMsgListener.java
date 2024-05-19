//package garry.mall.listener;
//
//import com.google.gson.Gson;
//import garry.mall.pojo.PayInfo;
//import garry.mall.service.impl.OrderServiceImpl;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * @author Garry
// * ---------2024/3/12 21:42
// **/
//@Slf4j
//@Component
//@RabbitListener(queues = {"payNotify"})//监听的消息队列
//public class PayMsgListener {
//
//    private final Gson gson = new Gson();
//
//    @Resource
//    private OrderServiceImpl orderService;
//
//    @RabbitHandler//接收payInfo消息的方法
//    public void process(String msg) {
//        log.info("【从消息队列获取的消息】：{}", msg);
//
//        PayInfo payInfo = gson.fromJson(msg, PayInfo.class);
//
//        if (payInfo.getPlatformStatus().equals("支付成功")) {
//            orderService.paid(payInfo.getOrderNo());
//        }
//    }
//}
