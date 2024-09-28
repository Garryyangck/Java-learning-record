package garry.train.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author Garry
 * 2024-09-28 21:19
 */
@Slf4j
@DisallowConcurrentExecution // 禁止任务并发执行
public class DailyTrainJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
