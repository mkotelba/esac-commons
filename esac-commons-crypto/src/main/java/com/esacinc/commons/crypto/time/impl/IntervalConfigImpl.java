package com.esacinc.commons.crypto.time.impl;

import com.esacinc.commons.crypto.time.IntervalConfig;
import javax.annotation.Nonnegative;

public class IntervalConfigImpl extends AbstractIntervalDescriptor implements IntervalConfig {
    private long offset;

    @Override
    public void setDuration(@Nonnegative long duration) {
        this.duration = duration;
    }

    @Override
    public long getOffset() {
        return this.offset;
    }

    @Override
    public void setOffset(long offset) {
        this.offset = offset;
    }
}
