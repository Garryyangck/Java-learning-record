package garry.pay.config;

/**
 * @author Garry
 * ---------2024/3/6 15:28
 **/

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 获取BestPayService
 */
@Component
public class BestPayConfig {
    @Resource
    private WxAccountConfig wxAccountConfig;

    @Bean//Spring管理，在项目启动时自动执行，只执行一次，注意命名又要求
    public BestPayService bestPayService(WxPayConfig wxPayConfig) {
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wxPayConfig);
        return bestPayService;
    }

    @Bean
    public WxPayConfig wxPayConfig() {
        WxPayConfig wxPayConfig = new WxPayConfig();
        //配置支付商户资料
        wxPayConfig.setAppId(wxAccountConfig.getAppId());//公众号appId
        wxPayConfig.setMchId(wxAccountConfig.getMchId());//商户号
        wxPayConfig.setMchKey(wxAccountConfig.getMchKey());//商户密钥
        //接收支付平台异步通知的地址
        //127.0.0.1为内网地址，无法被外网访问
        //可以通过natapp穿透内网，用其提供的域名代替此处的127.0.0.1即可接收支付平台的异步通知
        //刚花了10块钱租了个隧道把本机127.0.0.1打通了
        //注意测试的时候要保证natapp处于启动状态
        wxPayConfig.setNotifyUrl(wxAccountConfig.getNotifyUrl());
        wxPayConfig.setReturnUrl(wxAccountConfig.getReturnUrl());
        return wxPayConfig;
    }
}
