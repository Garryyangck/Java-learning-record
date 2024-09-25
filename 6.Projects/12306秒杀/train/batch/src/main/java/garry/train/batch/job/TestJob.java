package garry.train.batch.job;

/**
 * @author Garry
 * 2024-09-25 11:26
 */

import garry.train.common.consts.CommonConst;
import garry.train.common.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.MDC;

/**
 * 使用 quartz 的测试 Job
 */
@Slf4j
@DisallowConcurrentExecution // 禁止任务并发执行
public class TestJob implements Job {

    private static final Long baseTimeSecond;

    static {
        baseTimeSecond = System.currentTimeMillis() / 1000;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String logId = CommonUtil.generateUUID(CommonConst.LOG_ID_LENGTH);
        MDC.put("LOG_ID", logId);
        log.info("开始时间: {} 秒", System.currentTimeMillis() / 1000 - baseTimeSecond);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException("被唤醒了");
        }
        log.info("结束时间: {} 秒", System.currentTimeMillis() / 1000 - baseTimeSecond);
    }
}
