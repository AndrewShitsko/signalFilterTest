package shitsko.filter.api;

import org.junit.Test;
import test.filter.api.Filter;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class SignalFilterTest {

    private static class TestProducer extends Thread {

        private final Filter filter;
        private final AtomicInteger countSuccessSignals;

        private final int countAllSignals;
        private final long interval;

        public TestProducer(Filter filter, AtomicInteger countSuccessSignals, final int countAllSignals, final long interval) {
            this.filter = filter;
            this.countSuccessSignals = countSuccessSignals;
            this.countAllSignals = countAllSignals;
            this.interval = interval;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < countAllSignals; i++) {
                    if (filter.isSignalAllowed()) {
                        countSuccessSignals.getAndIncrement();
                    }
                    Thread.sleep(interval);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkFilter(final int limit, final int numberProducers, final long interval, final int numberSignals, final long expectedValue) throws InterruptedException {
        Filter filter = new SignalFilter(limit);

        AtomicInteger countSuccessSignals = new AtomicInteger();
        Thread [] producers = new Thread[numberProducers];
        for (int i = 0; i < producers.length; i++)
            producers[i] = new SignalFilterTest.TestProducer(filter, countSuccessSignals, numberSignals, interval);

        for (Thread producer : producers)
            producer.start();

        for (Thread producer : producers)
            producer.join();

        assertEquals(expectedValue, countSuccessSignals.get());
        System.out.println("Filter allowed " + countSuccessSignals + " signals, expected " + expectedValue + " signals");
    }

    @Test(timeout = 3 * 60 * 1000)
    public void testNumberSignalsLessLimit() throws InterruptedException {
        final int limit = 100;
        final int numberProducers = 1;
        final long interval = 1000;
        final int numberSignals = 60;
        final long expectedValue = numberSignals; // because numberSignals less than limit

        checkFilter(limit, numberProducers, interval, numberSignals, expectedValue);
    }

    @Test(timeout = 3 * 60 * 1000)
    public void test_2Threads_500Interval() throws InterruptedException {
        final int limit = 100;
        final int numberProducers = 2;
        final long interval = 500;
        final int numberSignals = 240;
        final long expectedValue = (long) (limit * Math.ceil(numberSignals / (1000d / interval) / 60d));

        checkFilter(limit, numberProducers, interval, numberSignals, expectedValue);
    }

    @Test(timeout = 3 * 60 * 1000)
    public void test_3Threads_500Interval_ChangeLimit() throws InterruptedException {
        final int limit = 101;
        final int numberProducers = 3;
        final long interval = 500;
        final int numberSignals = 240;
        final long expectedValue = (long) (limit * Math.ceil(numberSignals / (1000d / interval) / 60d));

        checkFilter(limit, numberProducers, interval, numberSignals, expectedValue);
    }

    @Test(timeout = 10 * 60 * 1000)
    public void test_10Threads_1500Interval() throws InterruptedException {
        final int limit = 120;
        final int numberProducers = 10;
        final long interval = 1500;
        final int numberSignals = 300;
        final long expectedValue = (long) (limit * Math.ceil(numberSignals / (1000d / interval) / 60d));

        checkFilter(limit, numberProducers, interval, numberSignals, expectedValue);
    }

    @Test(timeout = 2 * 60 * 1000)
    public void test_5Threads_LessThan_1Minute() throws InterruptedException {
        final int limit = 50;
        final int numberProducers = 5;
        final long interval = 100;
        final int numberSignals = 500;
        final long expectedValue = (long) (limit * Math.ceil(numberSignals / (1000d / interval) / 60d));

        checkFilter(limit, numberProducers, interval, numberSignals, expectedValue);
    }
}
