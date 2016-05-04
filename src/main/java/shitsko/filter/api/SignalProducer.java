package shitsko.filter.api;

import test.filter.api.Filter;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by AnriShitsko on 21.04.2016.
 */
public class SignalProducer extends Thread {

    private final Filter filter;
    private final AtomicInteger countSuccessSignals;
    private final AtomicInteger countAllSignals = new AtomicInteger();

    private BlockingQueue<Signal> signals;

    // constructor with arguments to initialize objects
    public SignalProducer(Filter filter, AtomicInteger countSuccessSignals, BlockingQueue<Signal> signals) {
        this.filter = filter;
        this.countSuccessSignals = countSuccessSignals;
        //this.countAllSignals = countAllSignals;
        this.signals = signals;
    }

    // method of the thread that receive signals and count successfully received signals and all signals
    @Override
    public void run() {
        Random random = new Random();
        try {
            while(true) {
                signals.put(new Signal(countAllSignals.incrementAndGet(), System.currentTimeMillis()));
                if (filter.isSignalAllowed()) {
                    countSuccessSignals.incrementAndGet();
                }
                Thread.sleep(random.nextInt(500));
            }
        } catch (InterruptedException e) {  }
    }

}
