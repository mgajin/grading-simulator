package app;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.*;

public class Student extends Thread {

    int id;
    int score;
    ScheduledExecutorService executor;

    public Student(int id) {
        this.id = id;
        this.score = 0;
        this.executor = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public void run() {

        int delay = new Random().nextInt(500) + 500;

        Future<Integer> future = executor.schedule(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return new Random().nextInt(11);
            }
        }, delay, TimeUnit.MILLISECONDS);

        try {
            this.score = future.get();
            System.out.println("Student[" + this.id + "] finished whit score: " + this.score + " at: " + new Date() + ", delay: " + delay);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
