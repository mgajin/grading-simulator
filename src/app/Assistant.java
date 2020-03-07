package app;

import java.util.concurrent.Semaphore;

public class Assistant extends Thread {

    Semaphore semaphore;

    public Assistant() {
        semaphore = new Semaphore(1, true);
    }

}
