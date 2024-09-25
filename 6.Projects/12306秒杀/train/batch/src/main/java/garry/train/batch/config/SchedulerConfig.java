package garry.train.batch.config;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author Garry
 * 2024-09-25 14:19
 */
@Configuration
public class SchedulerConfig {

    @Resource
    private MyJobFactory myJobFactory;

    /**
     * 将 Quartz 的 SchedulerFactory 集成官方提供的 Mysql 数据源后的 Bean
     * @param dataSource Quartz 官方提供的 Mysql 数据源
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("dataSource") DataSource dataSource) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setJobFactory(myJobFactory);
        factory.setStartupDelay(2); // 启动之后多少秒，开始执行 Quartz
        return factory;
    }
}
