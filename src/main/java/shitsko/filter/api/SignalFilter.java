package shitsko.filter.api;

import test.filter.api.Filter;

import java.util.concurrent.BlockingQueue;

/**
 * Created by AnriShitsko on 21.04.2016.
 */
public class SignalFilter implements Filter {

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
        long now = System.currentTimeMillis();
        long condition = now - 60000;

        int count = getCountByCondition(signals, condition);

        if (count < limit) {
            return true;
        }

        removeExpiredSignals(signals, now);

        return false;
    }

    private synchronized int getCountByCondition(BlockingQueue<Signal> signals, long condition) {
        int count = 0;
        for (Signal signal : signals) {
            if (signal.getSentTime() > condition)
                count++;
        }

        return count;
    }

    private synchronized void removeExpiredSignals(BlockingQueue<Signal> signals, long now) {
        for (Signal signal : signals) {
            long expiredTime = signal.getExpiredTime();
            System.out.println("Remove: now = " + now + "; expired =  " + expiredTime);
            if (now > expiredTime) {
                signals.remove(signal);
                System.out.println("Signal " + signal.getId() + " is removed!");
            }
        }
    }

}
