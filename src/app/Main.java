package app;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        int n = 6;
        List<Student> students = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Student student = new Student(i);
            students.add(student);
            student.start();
        }

        try {
            Thread.sleep(3000);
            double averageScore = 0;
            for (Student student : students) {
                averageScore += student.score;
            }
            averageScore /= n;
            System.out.println("Average score: " + averageScore);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
