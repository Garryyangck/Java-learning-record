package garry.train.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Garry
 * 2024-09-25 10:47
 */

/**
 * 缺点，适合单体应用，不适合集群。
 * 原因：我们生成每日报表的跑批任务，只需要跑一次，如果每个节点上都部署的话，就会重复执行。
 * 针对该问题的一个解决方法是：借助 redis 使用分布式锁，其它拿不到锁就无法执行。
 * 还有一个问题是：无法主动地暂停、继续执行，以及修改跑批时间。
 */
@Slf4j
@Component
//@EnableScheduling
public class SpringBootTestJob {

    /**
     * 定义一个定时任务，每 2 秒执行一次
     */
    @Scheduled(fixedRate = 2000)
    public void reportCurrentTime() {
        // 在此加分布式锁
        log.info("当前时间: {}", System.currentTimeMillis() / 1000);
    }

    /**
     * 使用 cron，秒、分、时、日、月、星期
     * 每分钟的秒数除以 2，余数为 0 则执行
     */
    @Scheduled(cron = "0/2 * * * * *")
    public void executeTaskWithCron() {
        // 在此加分布式锁
        log.info("执行任务: {}", System.currentTimeMillis() / 1000);
    }
}
