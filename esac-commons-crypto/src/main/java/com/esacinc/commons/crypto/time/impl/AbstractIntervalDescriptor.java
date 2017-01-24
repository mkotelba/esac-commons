package com.esacinc.commons.crypto.time.impl;

import com.esacinc.commons.beans.impl.AbstractDescriptorBean;
import com.esacinc.commons.crypto.time.IntervalDescriptor;

public abstract class AbstractIntervalDescriptor extends AbstractDescriptorBean implements IntervalDescriptor {
    protected long duration;

    protected AbstractIntervalDescriptor() {
        super(null, null);
    }

    @Override
    public long getDuration() {
        return this.duration;
    }
}
