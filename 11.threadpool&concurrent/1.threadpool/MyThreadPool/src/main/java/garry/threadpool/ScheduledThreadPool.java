package garry.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Garry
 * ---------2024/4/28 14:39
 **/
public class ScheduledThreadPool {
    public static void main(String[] args) {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);
        pool.schedule(new Task(), 1, TimeUnit.SECONDS);
        pool.scheduleAtFixedRate(new Task(), 1, 3, TimeUnit.SECONDS);
        pool.shutdown();
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread());
        }
    }
}
