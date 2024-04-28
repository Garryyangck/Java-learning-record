package garry.threadpool;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Garry
 * ---------2024/4/28 14:59
 **/
public class Shutdown {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            threadPool.execute(new Shutdown.Task());
        }
        Thread.sleep(2000);
        List<Runnable> unExecutedTaskList = threadPool.shutdownNow();
        for (Runnable runnable : unExecutedTaskList) {
            System.out.println(runnable);
        }
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(500);
                System.out.println(Thread.currentThread());
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread() + " is interrupted!");
            }
        }
    }
}
