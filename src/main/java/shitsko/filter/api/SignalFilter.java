package shitsko.filter.api;

import test.filter.api.Filter;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by AnriShitsko on 21.04.2016.
 */
public class SignalFilter implements Filter {

    // create the thread-local variable with initialValue = 0
    private static final ThreadLocal<Integer> counter = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    // filter limit
    private final int limit;

    // constructor with argument that initialize limit variable
    public SignalFilter(int limit) {
        this.limit = limit;
    }

    // implement method from the interface Filter
    @Override
    public boolean isSignalAllowed() {
        int count = counter.get();
        if (count < limit)
        {
            counter.set(count + 1);
            return true;
        }
        return false;
    }
}
