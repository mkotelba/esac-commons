package com.esacinc.commons.crypto.time;

import com.esacinc.commons.beans.ConfigBean;
import javax.annotation.Nonnegative;

public interface IntervalConfig extends ConfigBean, IntervalDescriptor {
    public void setDuration(@Nonnegative long duration);

    public long getOffset();

    public void setOffset(long offset);
}
