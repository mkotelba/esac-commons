package com.esacinc.commons.logging.metrics;

import com.codahale.metrics.Gauge;
import javax.annotation.Nullable;

public interface MutableGauge<T> extends Gauge<T> {
    public boolean hasValue();

    @Nullable
    @Override
    public T getValue();

    public void setValue(@Nullable T value);
}
