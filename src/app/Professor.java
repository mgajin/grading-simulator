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
            e.printStackTrace();
            this.semaphore.release();
        }
    }

    public boolean tryAcquire() {
        return semaphore.tryAcquire();
    }

    public void release() {
        this.semaphore.release();
    }

    public void await(int timeout) {
        try {
            barrier.await(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
            this.semaphore.release();
        }
    }

    public void reset() {
        barrier.reset();
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
