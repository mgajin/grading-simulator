package app;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Assistant implements Runnable {

    private Semaphore semaphore;
    private CountDownLatch latch;
    private AtomicBoolean running;

    public Assistant(CountDownLatch latch) {
        semaphore = new Semaphore(1, true);
        this.latch = latch;
        running = new AtomicBoolean(false);
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

        System.out.println("Assistant running: " + isRunning());
    }
}
