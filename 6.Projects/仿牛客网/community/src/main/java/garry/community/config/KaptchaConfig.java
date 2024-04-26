package garry.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Garry
 * ---------2024/3/18 11:02
 **/
@Slf4j
@Configuration//采用Java Config
public class KaptchaConfig {

    @Bean
    public Producer kaptchaProducer/*Bean的名字*/() {
        //DefaultKaptcha实现了Producer接口，Producer接口中有createImage方法
        DefaultKaptcha kaptcha = new DefaultKaptcha();

        //自定义Properties类
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", "100");//宽度
        properties.setProperty("kaptcha.image.height", "40");//高度
        properties.setProperty("kaptcha.textproducer.font.size", "32");//字体大小
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0");//颜色，RGB
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789ABCDEFGHJKMNPQRTUVWXYZ");//验证码的字符集
        properties.setProperty("kaptcha.textproducer.char.length", "4");//验证码长度
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");//干扰方式，这里选择没有额外干扰
        try {
            properties.store(new FileOutputStream("src/main/resources/kaptcha.properties"), null);
        } catch (IOException e) {
            log.error("【kaptcha配置文件存储失败】");
            e.printStackTrace();
        }

        //Config类，接收Properties自定义的配置
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
