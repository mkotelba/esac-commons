package com.esacinc.commons.crypto.time;

import com.esacinc.commons.beans.DescriptorBean;
import javax.annotation.Nonnegative;

public interface IntervalDescriptor extends DescriptorBean {
    @Nonnegative
    public long getDuration();
}
