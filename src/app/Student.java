package app;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.*;

public class Student extends Thread {

    int id;
    int score;
    ScheduledExecutorService executor;
    Semaphore assistant;
    Professor professor;

    public Student(int id, Assistant assistant, Professor professor) {
        this.id = id;
        this.score = 0;
        this.executor = new ScheduledThreadPoolExecutor(1);
        this.assistant = assistant.semaphore;
        this.professor = professor;
    }

    public Future<Integer> task(int delay) {

        return executor.schedule(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return new Random().nextInt(11);
            }
        }, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {

//        int delay = new Random().nextInt(500) + 500;
        int delay = 2000;
        String who = "";

        try {
            if (new Random().nextBoolean()) {
                who = "assistant";
                assistant.acquire();
                this.score = task(delay).get();
                assistant.release();
            } else {
                who = "professor";
                professor.acquire();
                professor.await(5);
                this.score = task(delay).get();
                professor.release();
            }
            System.out.println(who + " - Student[" + this.id + "] finished whit score: " + this.score + " at: " + new Date() + ", delay: " + delay);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}