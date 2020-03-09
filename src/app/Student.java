package app;

import app.Assistant;
import app.Professor;
import app.Simulation;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeoutException;

public class Student extends Thread {

    private int id;
    private int score;
    private String tutor;
    private Assistant assistant;
    private Professor professor;
    private String started;
    private String finished;
    private int duration;
    private DateFormat dateFormat;

    public Student(int id, Assistant assistant, Professor professor) {
        this.id = id;
        this.assistant = assistant;
        this.professor = professor;
        dateFormat = new SimpleDateFormat("mm:ss");
    }

    @Override
    public void run() {
        while (Simulation.isRunning()) {
            score = new Random().nextBoolean() ? goProfessor() : goAssistant();
            if (score > 0) {
                printMe();
                break;
            }
        }
    }

    public int goProfessor() {
        tutor = "Professor";
        if (professor.tryAcquire()) {
            try {
                professor.await(1500);
                started = dateFormat.format(new Date());
                duration = new Random().nextInt(500) + 500;
                sleep(duration);
                professor.release();
                finished = dateFormat.format(new Date());
                return new Random().nextInt(10) + 1;
            } catch (BrokenBarrierException | TimeoutException e) {
                goAssistant();
            } catch (InterruptedException e) {
                professor.release();
            }
        }
        return 0;
    }

    public int goAssistant() {
        tutor = "Assistant";
        if (assistant.tryAcquire()) {
            try {
                started = dateFormat.format(new Date());
                duration = new Random().nextInt(500) + 500;
                sleep(duration);
                assistant.release();
                finished = dateFormat.format(new Date());
                return new Random().nextInt(10) + 1;
            } catch (InterruptedException e) {
                assistant.release();
            }
        }
        return 0;
    }

    public void printMe() {
        String log = MessageFormat.format("{0} - Student[{1}] -> score: {2} [{3}-{4}, {5}ms]", tutor, id, score, started, finished, duration);
        System.out.println(log);
    }

    public int getScore() {
        return score;
    }
}