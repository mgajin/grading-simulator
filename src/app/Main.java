package app;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        int n = 100;
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
