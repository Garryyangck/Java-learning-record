package garry.community;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Garry
 * ---------2024/3/30 10:09
 **/
@Slf4j
public class ThreadPoolTest extends CommunityApplicationTests {
    //JDK普通线程池
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    //JDK定时执行任务的线程池
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    //spring普通线程池
    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    //spring定制执行任务的线程池
    @Resource
    private ThreadPoolTaskScheduler taskScheduler;

    /**
     * Junit测试和main方法不一样，不会等子线程结束才退出，而是创建完线程直接就不管子线程地退出了；
     * 因此我们需要创建该sleep方法将Junit的线程挂起，等待子线程执行
     *
     * @param m sleep的时间，单位是：毫秒
     */
    private void sleep(long m) {
        try {
            log.info("【tread {} has began to sleep】", Thread.currentThread().getName());
            Thread.sleep(m);
            log.info("【tread {} has stopped sleeping】", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExecutorService() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                log.debug("【hello, executorService!】");
                sleep(100);
            }
        };

        for (int i = 0; i < 10; i++) {
            executorService.submit(task);
            log.debug("【the no.{} thread has began to execute!】", i + 1);
        }

        sleep(2 * 1000);
    }

    @Test
    public void testScheduledExecutorService() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                log.debug("【hello, scheduledExecutorService!】");
            }
        };

        scheduledExecutorService.scheduleAtFixedRate(task, 0, 1000, TimeUnit.MILLISECONDS);

        sleep(10000);
    }

    @Test
    public void testThreadPoolTaskExecutor() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                log.debug("【hello, ThreadPoolTaskExecutor!】");
            }
        };

        for (int i = 0; i < 10; i++) {
            taskExecutor.submit(task);
        }

        sleep(2 * 1000);
    }

    @Test
    public void testThreadPoolTaskScheduler() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                log.debug("【hello, ThreadPoolTaskScheduler!】");
            }
        };

        Date startTime = new Date(System.currentTimeMillis() + 1000);
        taskScheduler.scheduleAtFixedRate(task, startTime, 1000);

        sleep(10000);
    }
}
