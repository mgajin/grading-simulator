package app;

import app.Assistant;
import app.Professor;
import app.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation {

    private List<Student> students;
    private Assistant assistant;
    private Professor professor;
    private int duration;
    static AtomicBoolean isRunning;

    private ScheduledThreadPoolExecutor studentExe;
    private ExecutorService executor;
    private CountDownLatch latch;

    public Simulation() {
        duration = 5000;
        students = new ArrayList<>();
        latch = new CountDownLatch(2);
        professor = new Professor(latch);
        assistant = new Assistant(latch);
        studentExe = new ScheduledThreadPoolExecutor(5);
        executor = Executors.newFixedThreadPool(2);
        isRunning = new AtomicBoolean(false);
    }

    public void addStudents(int n) {
        for (int i = 0; i < n; i++) {
            Student student = new Student(i, assistant, professor);
            students.add(student);
        }
    }

    public void start(int n) {
        addStudents(n);

        executor.execute(professor);
        executor.execute(assistant);

        try {
            latch.await();
            isRunning.set(true);
            for (Student student : students) {
                studentExe.schedule(student, 300, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        int n = 10;
        double averageScore = 0;
        Simulation simulation = new Simulation();
        simulation.start(n);

        try {
            Thread.sleep(simulation.duration);
            isRunning.set(false);
            simulation.executor.shutdownNow();
            simulation.studentExe.shutdownNow();

            for (Student student : simulation.students) {
                averageScore += student.getScore();
            }
            averageScore /= n;
            System.out.println("Average score: " + averageScore);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
