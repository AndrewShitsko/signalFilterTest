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
        try {
            // get time at now in milliseconds
            long now = System.currentTimeMillis();

            // get count of received signals
            int count = signals.size();

            // if-block to check count of signals at limit
            if (count < limit) {
                // put success signal to the queue
                signals.put(now);

                return true;
            } else
                // remove expired signals
                removeExpiredSignals(signals, now);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    // method to remove expired signals
    private void removeExpiredSignals(BlockingQueue<Long> signals, long now) {
        signals.stream().filter(signal -> now > (signal + 60000L)).forEach(signals::remove);
    }

}
