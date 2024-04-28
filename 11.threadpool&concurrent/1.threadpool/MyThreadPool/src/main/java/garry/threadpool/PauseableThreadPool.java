package garry.threadpool;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Garry
 * ---------2024/4/28 15:23
 **/
public class PauseableThreadPool extends ThreadPoolExecutor {

    private boolean isPaused;

    private final ReentrantLock lock = new ReentrantLock();

    // 和当前的锁绑定
    private final Condition unpaused = lock.newCondition();

    /*
        子类的构造函数必须要调用父类的构造函数才能完成父类的构造
     */
    public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    /**
     * 在任务执行前判断当前线程是否处于暂停状态
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        lock.lock();
        try {
            while (isPaused) {
                unpaused.await(); // 让当前的线程暂停，直到被重新激活
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    /**
     * 让当前线程暂停
     */
    public void pause() {
        lock.lock();
        try {
            isPaused = true;
        } catch (Exception e) {
            lock.unlock();
        }
    }

    public void resume() {
        lock.lock();
        try {
            isPaused = false;
            unpaused.signal();
            unpaused.signalAll(); // 唤醒该Condition上全部的线程
        } catch (Exception e) {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        PauseableThreadPool threadPool = new PauseableThreadPool(10, 10, 0L, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("我被执行");
                try {
                    Thread.sleep(10);
                    System.out.println(this.getClass());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        for (int i = 0; i < 10000; i++) {
            threadPool.execute(runnable);
        }
        Thread.sleep(1500);
        threadPool.pause();
        System.out.println("线程池被暂停...");
        Thread.sleep(1500);
        threadPool.resume();
        System.out.println("线程池被恢复...");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PauseableThreadPool that = (PauseableThreadPool) o;
        return Objects.equals(lock, that.lock) &&
                Objects.equals(unpaused, that.unpaused);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lock, unpaused);
    }
}
