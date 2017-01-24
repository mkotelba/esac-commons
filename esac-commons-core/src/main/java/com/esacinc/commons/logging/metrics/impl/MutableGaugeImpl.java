package com.esacinc.commons.logging.metrics.impl;

import com.esacinc.commons.logging.metrics.MutableGauge;
import javax.annotation.Nullable;

public class MutableGaugeImpl<T> implements MutableGauge<T> {
    protected T value;

    public MutableGaugeImpl() {
        this(null);
    }

    public MutableGaugeImpl(@Nullable T value) {
        this.value = value;
    }

    @Override
    public boolean hasValue() {
        return (this.value != null);
    }
    
    @Nullable
    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public void setValue(@Nullable T value) {
        this.value = value;
    }
}
