package com.esacinc.commons.management.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import javax.annotation.Nonnegative;

public class MemoryPoolUsage extends EsacMemoryUsage {
    public MemoryPoolUsage(Map<String, Object> data) {
        super(data);
    }

    public MemoryPoolUsage(long init, @Nonnegative long used, @Nonnegative long committed, long max) {
        super(init, used, committed, max);
    }

    @JsonProperty
    @Override
    public long getCommitted() {
        return super.getCommitted();
    }

    @JsonProperty("initial")
    @Override
    public long getInit() {
        return super.getInit();
    }
}
