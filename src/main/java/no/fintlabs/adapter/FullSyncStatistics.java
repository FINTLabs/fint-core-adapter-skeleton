package no.fintlabs.adapter;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class FullSyncStatistics {

    private int counter;

    private int maxCounters;

    private Random random;

    private Instant start;

    private static final int LIMIT = 5;

    public FullSyncStatistics() {
        random = new Random();
        init();
    }

    public void init() {
        counter = 0;
        maxCounters = 0;

        start = Instant.now();
    }

    public void increase() {
        while (counter >= LIMIT) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }

        counter++;
        if (counter > maxCounters) {
            maxCounters = counter;
        }

//        try {
//            Thread.sleep(random.nextInt(21));
//        } catch (InterruptedException e) {
//        }
    }

    public void decrease() {
        counter--;
    }

    public int getMaxCounters() {
        return maxCounters;
    }

    public int getCounter() {
        return counter;
    }

    public Duration end() {
        Instant finish = Instant.now();
        Duration timeElapsed = Duration.between(start, finish);
        return timeElapsed;
    }
}
