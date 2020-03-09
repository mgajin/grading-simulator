package app;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Simulation simulation = new Simulation();
        simulation.start(n);

        try {
            TimeUnit.SECONDS.sleep(5);
            simulation.stop();
            simulation.log();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
