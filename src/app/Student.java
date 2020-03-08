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
    private Assistant assistant;
    private Professor professor;
    private String finished;
    private DateFormat dateFormat;

    public Student(int id, Assistant assistant, Professor professor) {
        this.id = id;
        this.score = 0;
        this.assistant = assistant;
        this.professor = professor;
    }

    @Override
    public void run() {
        while (Simulation.isRunning.get()) {
            if (score == 0) {
                try {
                    String tutor;
                    if (new Random().nextBoolean()) {
                        tutor = "Assistant";
                        assistant.acquire();
                        sleep(2000);
                        score = new Random().nextInt(10) + 1;
                        assistant.release();
                    } else {
                        tutor = "Professor";
                        professor.acquire();
                        professor.await(1);
                        sleep(2000);
                        score = new Random().nextInt(10) + 1;
                        professor.release();
                    }
                    System.out.println(tutor + " - Student[" + id + "] -> score: " + score + " [" + new Date() + "]");
                } catch (InterruptedException e) {
                    System.out.println("Student interrupted");
                }
            }
        }
    }

    public int getScore() {
        return score;
    }
}