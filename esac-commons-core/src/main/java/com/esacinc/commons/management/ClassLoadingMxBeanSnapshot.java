package com.esacinc.commons.management;

import java.lang.management.ClassLoadingMXBean;
import javax.annotation.Nonnegative;

public interface ClassLoadingMxBeanSnapshot extends ClassLoadingMXBean, MxBeanSnapshot {
    @Nonnegative
    @Override
    public int getLoadedClassCount();

    @Nonnegative
    @Override
    public long getTotalLoadedClassCount();

    @Nonnegative
    @Override
    public long getUnloadedClassCount();
}
