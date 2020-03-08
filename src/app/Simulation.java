package app;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private List<Student> students;
    private Assistant assistant;
    private Professor professor;
    private int duration;

    public Simulation(int n) {
        this.students = new ArrayList<>();
        professor = new Professor();
        assistant = new Assistant();
        duration = 5000;

        addStudents(n);
    }

    public void start() {
        for (Student student : students) {
            student.start();
        }
    }

    public void addStudents(int n) {
        for (int i = 0; i < n; i++) {
            Student student = new Student(i, assistant, professor);
            students.add(student);
        }
    }

    public static void main(String[] args) {
        int n = 10;
        double averageScore = 0;
        Simulation simulation = new Simulation(n);
        simulation.start();

        try {
            Thread.sleep(simulation.duration);
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
