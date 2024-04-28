package garry.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Garry
 * ---------2024/4/28 14:31
 **/

/**
 * SingleThreadPool就是线程数为1的线程池
 */
public class SingleThreadPool {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Task());
        }
        executorService.shutdown();
        System.out.println("线程池关闭");
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread());
        }
    }
}
