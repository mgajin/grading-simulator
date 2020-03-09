package app;

import app.Assistant;
import app.Professor;
import app.Simulation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Student extends Thread {

    private int id;
    private int score;
    private String tutor;
    private Assistant assistant;
    private Professor professor;
    private String started;
    private String finished;
    private DateFormat dateFormat;

    public Student(int id, Assistant assistant, Professor professor) {
        this.id = id;
        this.score = 0;
        this.assistant = assistant;
        this.professor = professor;
        dateFormat = new SimpleDateFormat("mm:ss");
    }

    @Override
    public void run() {
//        while (Simulation.isRunning.get()) {
            try {
//                if (score == 0) {
                    present();
                    System.out.println(tutor + " - Student[" + id + "] -> score: " + score + " [" + started + "-" + finished +"]");
//                }
            } catch (InterruptedException e) {
                System.out.println("Student[" + id + "] interrupted");
            }
//        }
    }

    public void present () throws InterruptedException {
        started = dateFormat.format(new Date());
        int duration = new Random().nextInt(500) + 501;
        if (new Random().nextBoolean()) {
            tutor = "Assistant";
            assistant.acquire();
            sleep(duration);
            score = new Random().nextInt(10) + 1;
            assistant.release();
        } else {
            tutor = "Professor";
            professor.acquire();
            professor.await(6);
            sleep(duration);
            score = new Random().nextInt(10) + 1;
            professor.release();
        }
        finished = dateFormat.format(new Date());
    }

    public int getScore() {
        return score;
    }
}