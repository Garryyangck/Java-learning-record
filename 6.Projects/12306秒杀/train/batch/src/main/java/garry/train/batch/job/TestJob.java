package garry.train.batch.job;

/**
 * @author Garry
 * 2024-09-25 11:26
 */

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 使用 quartz 的测试 Job
 */
@Slf4j
public class TestJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("TestJob.execute TEST");
    }
}
