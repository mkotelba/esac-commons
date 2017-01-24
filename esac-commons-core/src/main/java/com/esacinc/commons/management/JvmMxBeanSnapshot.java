package com.esacinc.commons.management;

import com.esacinc.commons.beans.NamedBean;
import java.lang.management.RuntimeMXBean;
import javax.annotation.Nonnegative;

public interface JvmMxBeanSnapshot extends MxBeanSnapshot, NamedBean, RuntimeMXBean {
    public String getHostName();

    @Nonnegative
    public long getPid();

    @Nonnegative
    @Override
    public long getStartTime();

    @Nonnegative
    @Override
    public long getUptime();
}
