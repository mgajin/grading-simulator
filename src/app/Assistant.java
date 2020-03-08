package app;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Assistant extends Thread {

    private Semaphore semaphore;
    private CountDownLatch latch;

    public Assistant(CountDownLatch latch) {
        semaphore = new Semaphore(1, true);
        this.latch = latch;
    }

    public void acquire() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        semaphore.release();
    }

    @Override
    public void run() {
        latch.countDown();
        System.out.println("Assistant running: " + isAlive());
    }
}
