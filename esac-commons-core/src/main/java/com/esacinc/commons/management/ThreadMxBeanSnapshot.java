package com.esacinc.commons.management;

import com.sun.management.ThreadMXBean;
import java.lang.Thread.State;
import java.util.Map;
import javax.annotation.Nonnegative;

public interface ThreadMxBeanSnapshot extends MxBeanSnapshot, ThreadMXBean {
    @Nonnegative
    @Override
    public int getDaemonThreadCount();

    @Nonnegative
    public int getDeadlockedThreadCount();

    @Nonnegative
    public int getMonitorDeadlockedThreadCount();

    @Nonnegative
    public int getNativeThreadCount();

    public Map<State, Integer> getStateThreadCounts();

    @Nonnegative
    public int getSynchronizerDeadlockedThreadCount();

    @Nonnegative
    @Override
    public int getThreadCount();

    public Map<Long, EsacThreadInfo> getThreadInfos();

    @Nonnegative
    @Override
    public long getTotalStartedThreadCount();
}
