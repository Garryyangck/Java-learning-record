package garry.train.member.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.member.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Garry
 * 2024-09-09 21:33
 */

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {
    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.sms.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.sms.signName}")
    private String signName;

    @Value("${aliyun.sms.templateCode}")
    private String templateCode;

    //短信API产品名称（短信产品名固定，无需修改）
    private final String product = "Dysmsapi";

    //短信API产品域名（接口地址固定，无需修改）
    private final String domain = "dysmsapi.aliyuncs.com";

    @Override
    public void sendSms(String phoneNumber, String templateParam) {
        try {
            // 创建DefaultAcsClient实例并初始化
            DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient client = new DefaultAcsClient(profile);

            // 创建SendSmsRequest实例，并设置相应的参数
            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            request.setPhoneNumbers(phoneNumber);
            request.setSignName(signName);
            request.setTemplateCode(templateCode);
            request.setTemplateParam(templateParam);

            // 发起请求并处理响应
            SendSmsResponse response = client.getAcsResponse(request);
            if (!StringUtils.equals("OK", response.getCode())){
                log.error("[短信服务] 发送短信失败，手机号码：{}，原因：{}，response = {}", phoneNumber, response.getMessage(), JSONObject.toJSON(response));
                throw new BusinessException(ResponseEnum.MEMBER_MESSAGE_CODE_SEND_FAILED);
            } else {
                log.info("[短信服务] 发送短信成功，response = {}", JSONUtil.toJsonPrettyStr(response));
            }

        } catch (ClientException e) {
            log.error("[短信服务] 发送短信异常，手机号码：{}，错误码：{}，错误信息：{}", phoneNumber, e.getErrCode(), e.getErrMsg());
            throw new BusinessException(ResponseEnum.MEMBER_MESSAGE_CODE_SEND_FAILED);
        } catch (Exception e) {
            log.error("[短信服务] 发送短信异常，手机号码：{}", phoneNumber, e);
            throw new BusinessException(ResponseEnum.MEMBER_MESSAGE_CODE_SEND_FAILED);
        }
    }
}
