package garry.train.member.service;

/**
 * @author Garry
 * 2024-09-09 21:32
 */
public interface SmsService {
    /**
     * @param phoneNumber   手机号
     * @param templateParam 模板内的参数，这里的模板为 ${code}，但是注意必须为 JSON 字符串
     */
    void sendSms(String phoneNumber, String templateParam);
}
