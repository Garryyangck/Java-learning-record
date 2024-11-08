package garry.train.batch.job;

/**
 * @author Garry
 * 2024-09-25 11:26
 */

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * 使用 quartz 的测试 Job
 */
@Slf4j
@Component
@DisallowConcurrentExecution // 禁止任务并发执行
public class TestJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException("被唤醒了");
        }
    }
}
