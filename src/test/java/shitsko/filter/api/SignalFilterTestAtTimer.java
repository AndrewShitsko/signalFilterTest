package shitsko.filter.api;

import org.junit.Test;
import test.filter.api.Filter;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SignalFilterTestAtTimer {

    // timer for 3 minutes
    private static final long TIMER = 3 * 60 * 1000;

    // number of the producers-threads
    private static final int NUMBER_PRODUCERS = 3;

    // filter limit
    private static final int LIMIT = 100;

    // demonstrate a work of the Signal filter at timer
    @Test
    public void testSignalFilterAtTimer() {

        // queue for the received signals
        BlockingQueue<Signal> signals = new ArrayBlockingQueue<>(LIMIT);

        // create the filter
        Filter filter = new SignalFilter(LIMIT, signals);

        // counter for successfully received signals
        AtomicInteger countSuccessSignals = new AtomicInteger();
        AtomicInteger countAllSignals = new AtomicInteger();

        // create producers-threads
        Thread[] producers = new Thread[NUMBER_PRODUCERS];
        for (int i = 0; i < producers.length; i++)
            producers[i] = new SignalProducer(filter, countSuccessSignals, countAllSignals, signals, 1000); // the big constructor only for the test

        // create the timer
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    for (Thread producer : producers)
                        producer.start();
                    for (Thread producer : producers)
                        producer.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, TIMER);

        try{
            Thread.sleep(TIMER);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // stop the timer
        timer.cancel();

        // stop all producers-threads
        for (Thread producer : producers)
            producer.interrupt();

        System.out.println("Filter allowed " + countSuccessSignals + " out of " + countAllSignals);
    }
}
