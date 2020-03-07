package app;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.*;

public class Student extends Thread {

    int id;
    int score;
    ScheduledExecutorService executor;
    Semaphore assistant;

    public Student(int id, Assistant assistant) {
        this.id = id;
        this.score = 0;
        this.executor = new ScheduledThreadPoolExecutor(1);
        this.assistant = assistant.semaphore;
    }

    @Override
    public void run() {

        int delay = new Random().nextInt(500) + 500;

        try {
            assistant.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Future<Integer> future = executor.schedule(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return new Random().nextInt(11);
            }
        }, delay, TimeUnit.MILLISECONDS);

        try {
            this.score = future.get();
            System.out.println("Student[" + this.id + "] finished whit score: " + this.score + " at: " + new Date() + ", delay: " + delay);
            assistant.release();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
