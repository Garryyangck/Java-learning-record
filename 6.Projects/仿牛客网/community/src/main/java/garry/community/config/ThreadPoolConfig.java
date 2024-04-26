package garry.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Garry
 * ---------2024/3/30 11:11
 **/
@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}
