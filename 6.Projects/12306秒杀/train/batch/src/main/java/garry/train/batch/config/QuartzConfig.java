package garry.train.batch.config;

import garry.train.batch.job.TestJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;

/**
 * @author Garry
 * 2024-09-25 11:28
 */

/**
 * 该配置类只有在第一次被读取时，会将其配置内容存储到数据库中，
 * 之后就直接读取数据库获取配置，而不再读取此配置类了。
 * Quartz 发现数据库中有某个 Job 的 Detail 配置和触发器 Trigger 配置，就会自动的按配置执行该任务。
 * <p>
 * 但是这种方式是写死的，我们期望的是通过控制台创建跑批任务，而不是先写好跑批任务。
 * 因此该类只作为自行配置的示范，我们 batch 模块中不会使用这种提前写好配置的方式，创建跑批任务。
 * <p/>
 */
@Deprecated
//@Configuration
public class QuartzConfig {

    /**
     * 声明一个任务
     */
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(TestJob.class)
                .withIdentity("TestJob", "test")
                .storeDurably()
                .build();
    }

    /**
     * 声明一个触发器，什么时候执行任务
     */
    @Bean
    public Trigger trigger() {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withIdentity("TestJobTrigger", "trigger")
                .startNow()
                // 每2秒执行一次
                .withSchedule(CronScheduleBuilder.cronSchedule("*/2 * * * * ?"))
                .build();
    }
}
