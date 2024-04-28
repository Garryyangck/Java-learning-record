package garry.threadpool;

/**
 * @author Garry
 * ---------2024/4/28 11:44
 **/
public class ForLoop {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Task());
            thread.start();
        }
    }

    static class Task implements Runnable {
        @Override
        public void run() {
            System.out.println("执行了任务...");
        }
    }
}
