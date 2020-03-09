package app;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Simulation simulation = new Simulation();
        simulation.start(n);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            simulation.stop();
            simulation.log();
        }
    }
}
