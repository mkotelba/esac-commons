package com.esacinc.commons.logging.metrics.impl;

import com.codahale.metrics.Meter;
import com.esacinc.commons.time.utils.EsacDateUtils;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import javax.annotation.Nonnegative;

public class SamplingMeter extends Meter {
    private LongAdder count = new LongAdder();
    private AtomicLong startTimestamp;

    public SamplingMeter() {
        super();

        this.startTimestamp = new AtomicLong(System.nanoTime());
    }

    @Override
    public void mark(@Nonnegative long value) {
        this.count.add(value);
    }

    @Override
    public void mark() {
        this.count.increment();
    }

    @Nonnegative
    @Override
    public double getFifteenMinuteRate() {
        return 0.0D;
    }

    @Nonnegative
    @Override
    public double getFiveMinuteRate() {
        return 0.0D;
    }

    @Nonnegative
    @Override
    public double getOneMinuteRate() {
        return 0.0D;
    }

    @Nonnegative
    @Override
    public double getMeanRate() {
        long timestamp = System.nanoTime(), duration = (timestamp - this.startTimestamp.getAndSet(timestamp)), count = this.getCount();

        return ((count == 0L) ? 0.0D : ((count / duration) * EsacDateUtils.NS_IN_MS));
    }

    @Nonnegative
    @Override
    public long getCount() {
        return this.count.sumThenReset();
    }
}
