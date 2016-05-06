package shitsko.filter.api;

import test.filter.api.Filter;

import java.util.concurrent.atomic.AtomicInteger;

public class SignalProducer extends Thread {

    private final Filter filter;
    private final AtomicInteger countSuccessSignals;
    private final AtomicInteger countAllSignals;

    private final long milliseconds;

    // constructor with arguments to initialize objects
    public SignalProducer(Filter filter, AtomicInteger countSuccessSignals, AtomicInteger countAllSignals, long milliseconds) {
        this.filter = filter;
        this.countSuccessSignals = countSuccessSignals;
        this.countAllSignals = countAllSignals;
        this.milliseconds = milliseconds;
    }

    // method of the thread that receive signals and count successfully received signals and all signals
    @Override
    public void run() {
        try {
            while(true) {
                countAllSignals.getAndIncrement();
                if (filter.isSignalAllowed()) {
                    countSuccessSignals.incrementAndGet();
                }
                Thread.sleep(milliseconds);
            }
        } catch (InterruptedException e) {  }
    }

}
