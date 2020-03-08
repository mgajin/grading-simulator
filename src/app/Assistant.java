package app;

import java.util.concurrent.Semaphore;

public class Assistant extends Thread {

    private Semaphore semaphore;

    public Assistant() {
        semaphore = new Semaphore(1, true);
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

}
