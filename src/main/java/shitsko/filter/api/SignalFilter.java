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
    public boolean isSignalAllowed() {
        long condition = System.currentTimeMillis() - 60000; // one minute ago
        int count = countOnCondition(signals, condition);
        if (count < limit) {
            return true;
        }

        return false;
    }

    private int countOnCondition(BlockingQueue<Signal> signals, long condition) {
        int count = 0;
        for (Signal signal : signals) {
            if (signal.getTime() > condition)
                count++;
        }

        return count;
    }
}
