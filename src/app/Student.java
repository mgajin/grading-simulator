package app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.*;

public class Student extends Thread {

    private int id;
    private int score;
    private ScheduledExecutorService executor;
    private Assistant assistant;
    private Professor professor;
    private String finished;
    private DateFormat dateFormat;

    public Student(int id, Assistant assistant, Professor professor) {
        this.id = id;
        this.score = 0;
        this.executor = new ScheduledThreadPoolExecutor(1);
        this.assistant = assistant;
        this.professor = professor;
        dateFormat = new SimpleDateFormat("mm:ss");
    }

    public Future<Integer> task(int delay) {

        return executor.schedule(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                finished = dateFormat.format(new Date());
                return new Random().nextInt(10) + 1;
            }
        }, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {

//        int delay = new Random().nextInt(500) + 500;
        int duration = 2000;
        String who = "";

        try {
            String started;
            if (new Random().nextBoolean()) {
                who = "assistant";
                assistant.acquire();
                started = dateFormat.format(new Date());
                this.score = task(duration).get();
                assistant.release();
            } else {
                who = "professor";
                professor.acquire();
                professor.await(2);
                started = dateFormat.format(new Date());
                this.score = task(duration).get();
                professor.release();
            }
            System.out.println(who + " - Student[" + id + "] -> score:" + score +  " [" + started + "-" + finished + ", " + duration + "ms]");
            executor.shutdownNow();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Student[" + id + "interrupted, score:" + score + " [" + dateFormat.format(new Date()) + "]");
        }
    }

    public int getScore() {
        return score;
    }
}