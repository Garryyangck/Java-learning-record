package garry.train.websocket.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Garry
 * 2024-10-11 19:20
 */
@SpringBootApplication
@ComponentScan("garry") // 由于Application类放到了config包下，它只能扫描和自己同包的类，因此需要新增ComponentScan注解让其扫描整个garry包下的类
public class WebsocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebsocketApplication.class, args);
    }
}