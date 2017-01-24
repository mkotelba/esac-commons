package com.esacinc.commons.logging.metrics.impl;

import com.codahale.metrics.Counter;
import java.util.concurrent.atomic.LongAdder;
import javax.annotation.Nonnegative;

public class SamplingCounter extends Counter {
    private LongAdder count = new LongAdder();

    @Override
    public void inc(long value) {
        this.count.add(value);
    }

    @Override
    public void inc() {
        this.count.add(1L);
    }

    @Override
    public void dec(long value) {
        this.count.add(-value);
    }

    @Override
    public void dec() {
        this.count.add(-1L);
    }

    @Nonnegative
    @Override
    public long getCount() {
        return this.count.sumThenReset();
    }
}
