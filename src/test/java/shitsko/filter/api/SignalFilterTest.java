package shitsko.filter.api;

import org.junit.Test;
import test.filter.api.Filter;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by AnriShitsko on 21.04.2016.
 */

/**
 * The test for checking filter's limit
 */
public class SignalFilterTest {

    // one minute
    private static final long TIMER = 120 * 1000;

    // number of the producers-threads
    private static final int NUMBER_PRODUCERS = 1;

    // filter limit
    private static final int LIMIT = 100;

    @Test
    public void testSignalTest() {

        BlockingQueue<Signal> signals = new ArrayBlockingQueue<>(LIMIT);

        // create the filter
        Filter filter = new SignalFilter(LIMIT, signals);

        // counter for successfully received signals
        AtomicInteger countSuccessSignals = new AtomicInteger();

        // counter for all receiving signals
        AtomicInteger countAllSignals = new AtomicInteger();

        // create producers-threads
        Thread[] producers = new Thread[NUMBER_PRODUCERS];
        for (int i = 0; i < producers.length; i++)
            producers[i] = new SignalProducer(filter, countSuccessSignals, signals);

        // create the timer for one minute
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

        // wait a minute
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

        //assertEquals(500, countSuccessSignals.get());
        System.out.println("Filter allowed " + countSuccessSignals);
    }
}
