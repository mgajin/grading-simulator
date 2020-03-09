package app;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation {

    private List<Student> students;
    private Assistant assistant;
    private Professor professor;
    private static AtomicBoolean running;

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
        running = new AtomicBoolean(false);
    }

    public void start(int n) {
        System.out.println("Started [" +  new SimpleDateFormat("mm:ss").format(new Date())+ "]");
        executor.execute(professor);
        executor.execute(assistant);

        try {
            latch.await();
            setRunning(true);
            for (int i = 0; i < n; i++) {
                Student student = new Student(i, assistant, professor);
                students.add(student);
                studentExe.schedule(student, 500, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        setRunning(false);
        professor.setRunning(false);
        assistant.setRunning(false);
        executor.shutdownNow();
        studentExe.shutdownNow();
    }

    public void log() {
        double averageScore = 0;
        for (Student student : students) {
            averageScore += student.getScore();
        }
        averageScore /= students.size();
        System.out.println("Finished [" + new SimpleDateFormat("mm:ss").format(new Date()) + "]");
        System.out.println("Average Score: " + averageScore);
    }

    public void setRunning(Boolean running) {
        Simulation.running.set(running);
    }

    public static Boolean isRunning() {
        return running.get();
    }
}
