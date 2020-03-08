package app;

import java.util.concurrent.*;

public class Professor extends Thread {

    private CyclicBarrier barrier;
    private Semaphore semaphore;
    private CountDownLatch latch;

    public Professor(CountDownLatch latch) {
        barrier = new CyclicBarrier(2);
        semaphore = new Semaphore(2);
        this.latch = latch;
    }

    public void acquire() {
        try {
            this.semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        this.semaphore.release();
    }

    public void await(int timeout) {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch.countDown();
        System.out.println("Professor running: " + isAlive());
    }
}
