package app;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Professor implements Runnable {

    private CyclicBarrier barrier;
    private Semaphore semaphore;
    private CountDownLatch latch;
    private AtomicBoolean running;

    public Professor(CountDownLatch latch) {
        barrier = new CyclicBarrier(2);
        semaphore = new Semaphore(2);
        this.latch = latch;
        this.running = new AtomicBoolean(false);
    }

    public void acquire() {
        try {
            this.semaphore.acquire();
        } catch (InterruptedException e) {
            this.semaphore.release();
        }
    }

    public boolean tryAcquire() {
        return semaphore.tryAcquire();
    }

    public void release() {
        this.semaphore.release();
    }

    public void await(int timeout) throws InterruptedException, TimeoutException, BrokenBarrierException {
        barrier.await(timeout, TimeUnit.MILLISECONDS);
    }

    public Boolean isRunning() {
        return running.get();
    }

    public void setRunning(Boolean running) {
        this.running.set(running);
    }

    @Override
    public void run() {
        latch.countDown();
        setRunning(true);

        while (true) {
            if (!isRunning()) break;
        }
    }
}
