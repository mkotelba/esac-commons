package com.esacinc.commons.management.impl;

import com.esacinc.commons.utils.EsacMapUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.management.MemoryUsage;
import java.util.Map;
import javax.annotation.Nonnegative;

public class EsacMemoryUsage extends MemoryUsage {
    protected final static String COMMITTED_DATA_KEY = "committed";
    protected final static String INIT_DATA_KEY = "init";
    protected final static String MAX_DATA_KEY = "max";
    protected final static String USED_DATA_KEY = "used";

    protected long init;
    protected boolean initAvailable;
    protected long used;
    protected boolean usedAvailable;
    protected long committed;
    protected boolean committedAvailable;
    protected long max;
    protected boolean maxAvailable;
    protected long free;
    protected boolean freeAvailable;
    protected double usage;
    protected boolean usageAvailable;

    public EsacMemoryUsage(Map<String, Object> data) {
        // noinspection ConstantConditions
        this(EsacMapUtils.getOrDefault(data, INIT_DATA_KEY, -1L), EsacMapUtils.getOrDefault(data, USED_DATA_KEY, 0L),
            EsacMapUtils.getOrDefault(data, COMMITTED_DATA_KEY, 0L), EsacMapUtils.getOrDefault(data, MAX_DATA_KEY, -1L));
    }

    public EsacMemoryUsage(@Nonnegative long used, long max, long free) {
        this(-1L, used, 0L, max, free, -1.0D);
    }

    public EsacMemoryUsage(long init, @Nonnegative long used, @Nonnegative long committed, long max) {
        this(init, used, committed, max, -1L, -1.0D);
    }

    public EsacMemoryUsage(long init, @Nonnegative long used, @Nonnegative long committed, long max, long free, double usage) {
        super(-1L, 0L, 0L, -1L);

        this.initAvailable = ((this.init = init) >= 0);
        this.usedAvailable = ((this.used = used) >= 0);
        this.committedAvailable = ((this.committed = committed) >= 0);
        this.maxAvailable = ((this.max = max) > 0);
        this.freeAvailable = ((this.free = free) >= 0);
        this.usageAvailable = ((this.usage = usage) >= 0.0D);

        if (this.maxAvailable) {
            if (this.usedAvailable) {
                if (!this.freeAvailable) {
                    this.free = (this.max - this.used);
                    this.freeAvailable = true;
                }
            } else if (this.freeAvailable) {
                this.used = (this.max - this.free);
                this.usedAvailable = true;
            }

            if (!this.usageAvailable && this.usedAvailable) {
                this.usage = (this.used / this.max);
                this.usageAvailable = true;
            }
        }
    }

    public boolean hasCommitted() {
        return this.committedAvailable;
    }

    @Override
    public long getCommitted() {
        return this.committed;
    }

    public boolean hasFree() {
        return this.freeAvailable;
    }

    @JsonProperty
    public long getFree() {
        return this.free;
    }

    public boolean hasInit() {
        return this.initAvailable;
    }

    @Override
    public long getInit() {
        return this.init;
    }

    public boolean hasMax() {
        return this.maxAvailable;
    }

    @JsonProperty
    @Override
    public long getMax() {
        return this.max;
    }

    public boolean hasUsage() {
        return this.usageAvailable;
    }

    @JsonProperty
    public double getUsage() {
        return this.usage;
    }

    public boolean hasUsed() {
        return this.usedAvailable;
    }

    @JsonProperty
    @Override
    public long getUsed() {
        return this.used;
    }
}
