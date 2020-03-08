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

    static ScheduledThreadPoolExecutor studentExe;
    static ExecutorService executor;
    CountDownLatch latch;

    public Simulation(int n) {
        latch = new CountDownLatch(2);
        this.students = new ArrayList<>();
        professor = new Professor(latch);
        assistant = new Assistant(latch);
        duration = 5000;
        isRunning = new AtomicBoolean(false);

        studentExe = new ScheduledThreadPoolExecutor(5);
        executor = Executors.newFixedThreadPool(2);

        addStudents(n);
    }

    public void addStudents(int n) {
        for (int i = 0; i < n; i++) {
            Student student = new Student(i, assistant, professor);
            students.add(student);
        }
    }

    public void start() {
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
        Simulation simulation = new Simulation(n);
        simulation.start();

        try {
            Thread.sleep(simulation.duration);
            isRunning.set(false);
            executor.shutdownNow();
            studentExe.shutdownNow();
            System.out.println("Simulation finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
