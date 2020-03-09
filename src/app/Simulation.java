package app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation {

    private List<Student> students;
    private Assistant assistant;
    private Professor professor;
    static AtomicBoolean isRunning;

    private ScheduledThreadPoolExecutor studentExe;
    private ExecutorService executor;
    private CountDownLatch latch;

    public Simulation() {
        students = new ArrayList<>();
        latch = new CountDownLatch(2);
        professor = new Professor(latch);
        assistant = new Assistant(latch);
        studentExe = new ScheduledThreadPoolExecutor(5);
        executor = Executors.newFixedThreadPool(2);
        isRunning = new AtomicBoolean(false);
    }

    public void start(int n) {
        System.out.println("Started [" + new Date() + "]");
        executor.execute(professor);
        executor.execute(assistant);

        try {
            latch.await();
            isRunning.set(true);
            for (int i = 0; i < n; i++) {
                Student student = new Student(i, assistant, professor);
                students.add(student);
                studentExe.schedule(student, 200, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        executor.shutdownNow();
        studentExe.shutdownNow();
        isRunning.set(false);
        professor.setRunning(false);
        assistant.setRunning(false);
    }

    public void log() {
        double averageScore = 0;
        for (Student student : students) {
            averageScore += student.getScore();
        }
        averageScore /= students.size();
        System.out.println("Average score: " + averageScore + " [" + new Date() + "]");
    }
}
