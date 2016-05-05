package shitsko.filter.api;

import test.filter.api.Filter;

import java.util.concurrent.BlockingQueue;

public class SignalFilter implements Filter {

    // queue for the receive signals
    private BlockingQueue<Signal> signals;

    // filter limit
    private final int limit;

    // constructor with argument that initialize limit variable
    public SignalFilter(int limit, BlockingQueue<Signal> signals) {
        this.limit = limit;
        this.signals = signals;
    }

    // implement method from the interface Filter
    @Override
    public synchronized boolean isSignalAllowed() {
        // get time at now in milliseconds
        long now = System.currentTimeMillis();

        // get count of received signals
        int count = signals.size();

        // if-block to check count of signals at limit
        if (count < limit) return true;

        // remove expired signals
        removeExpiredSignals(signals, now);

        return false;
    }

    // method to remove expired signals
    private void removeExpiredSignals(BlockingQueue<Signal> signals, long now) {
        signals.stream().filter(signal -> now > (signal.getTime() + 60000)).forEach(signals::remove);
    }

}
