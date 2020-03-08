package app;

import java.util.concurrent.*;

public class Professor extends Thread {

    private CyclicBarrier barrier;
    private Semaphore semaphore;

    public Professor() {
        barrier = new CyclicBarrier(2);
        semaphore = new Semaphore(2);
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
            this.barrier.await(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Barrier has broken");
    }
}
