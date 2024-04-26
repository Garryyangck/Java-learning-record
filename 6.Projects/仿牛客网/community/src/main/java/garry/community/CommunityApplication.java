package garry.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@MapperScan(basePackages = "garry.community.dao")
public class CommunityApplication {

    @PostConstruct
    public void init() {
        //解决netty启动冲突问题
        //该配置在 Netty4Utils.setAvailableProcessors 中得到
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

}
