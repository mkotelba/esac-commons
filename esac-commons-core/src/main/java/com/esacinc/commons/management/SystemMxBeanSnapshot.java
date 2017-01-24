package com.esacinc.commons.management;

import com.esacinc.commons.beans.NamedBean;
import com.sun.management.OperatingSystemMXBean;
import javax.annotation.Nonnegative;

public interface SystemMxBeanSnapshot extends MxBeanSnapshot, NamedBean, OperatingSystemMXBean {
    @Nonnegative
    @Override
    public int getAvailableProcessors();

    @Nonnegative
    @Override
    public long getCommittedVirtualMemorySize();

    @Nonnegative
    @Override
    public long getFreePhysicalMemorySize();

    @Nonnegative
    @Override
    public long getFreeSwapSpaceSize();

    public long getMaxFileDescriptorCount();

    public long getOpenFileDescriptorCount();

    @Nonnegative
    @Override
    public double getProcessCpuLoad();

    @Nonnegative
    @Override
    public long getProcessCpuTime();

    @Nonnegative
    @Override
    public double getSystemCpuLoad();

    @Nonnegative
    @Override
    public long getTotalPhysicalMemorySize();

    @Nonnegative
    @Override
    public long getTotalSwapSpaceSize();

    public String[] getUnixAttributeNames();
}
