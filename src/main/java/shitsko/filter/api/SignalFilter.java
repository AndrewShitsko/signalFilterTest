package shitsko.filter.api;

import test.filter.api.Filter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SignalFilter implements Filter {

    // queue for the receive signals
    private BlockingQueue<Long> signals;

    // filter limit
    private final int limit;

    // constructor with argument that initialize limit variable
    public SignalFilter(int limit) {
        this.limit = limit;
        signals = new ArrayBlockingQueue<>(limit);
    }

    // implement method from the interface Filter
    @Override
    public synchronized boolean isSignalAllowed() {

            // get time at now in milliseconds
            long now = System.currentTimeMillis();

            // add signals to the queue, otherwise remove expired signals
            if (signals.offer(now)) {
                return true;
            } else
                // remove expired signals
                removeExpiredSignals(now);

        return false;
    }

    // method to remove expired signals
    private synchronized void removeExpiredSignals(long now) {
        signals.removeIf(s -> now > s.longValue() + 60000L);
    }

}
