package com.esacinc.commons.management;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.esacinc.commons.management.impl.EsacMemoryUsage;
import java.lang.management.BufferPoolMXBean;
import javax.annotation.Nonnegative;

public interface BufferPoolMxBeanSnapshot extends BufferPoolMXBean, PoolMxBeanSnapshot {
    @JsonProperty
    @Nonnegative
    @Override
    public long getCount();

    @Nonnegative
    @Override
    public long getMemoryUsed();

    @Nonnegative
    @Override
    public long getTotalCapacity();

    @JsonProperty
    @JsonUnwrapped
    public EsacMemoryUsage getUsage();
}
