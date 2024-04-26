package garry.community.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * @author Garry
 * ---------2024/3/16 18:48
 **/

/**
 * 委托新浪邮箱完成发邮件的功能，因此该类相当于一个客户端
 */
@Slf4j
@Component
public class MailClient {
    @Resource
    private JavaMailSender mailSender;//java邮件的核心Bean

    @Value("${spring.mail.username}")
    private String sender;//发送者，就是新浪邮箱的网址

    public void sendMail(String receiver, String subject, String content) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject(subject);
            helper.setText(content, true/*使用html格式传输*/);
            helper.setSentDate(new Date());
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("【邮件发送失败】 MessagingException = {}", e.getLocalizedMessage());
            e.printStackTrace();
            throw e;//抛出异常，便于上级收到异常，以执行其业务逻辑
        }
    }
}
