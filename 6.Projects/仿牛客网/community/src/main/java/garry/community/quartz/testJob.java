package garry.community.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author Garry
 * ---------2024/3/30 12:02
 **/
@Slf4j
public class testJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("【thread {} is executing】", Thread.currentThread().getName());
    }
}
