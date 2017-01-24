package com.esacinc.commons.management.impl;

import com.esacinc.commons.utils.EsacMapUtils;
import com.esacinc.commons.management.EsacThreadInfo;
import com.esacinc.commons.management.ThreadMxBeanSnapshot;
import com.esacinc.commons.management.utils.EsacManagementUtils;
import java.lang.Thread.State;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.Map;
import javax.annotation.Nonnegative;
import javax.management.AttributeList;
import javax.management.ObjectName;
import org.apache.commons.lang3.ArrayUtils;

public class ThreadMxBeanSnapshotImpl extends AbstractMxBeanSnapshot implements ThreadMxBeanSnapshot {
    public final static ObjectName OBJ_NAME = EsacManagementUtils.buildObjectName(ManagementFactory.THREAD_MXBEAN_NAME);

    public final static String THREAD_COUNT_ATTR_NAME = "Thread" + COUNT_ATTR_NAME;
    public final static String DAEMON_THREAD_COUNT_ATTR_NAME = "Daemon" + THREAD_COUNT_ATTR_NAME;
    public final static String TOTAL_STARTED_THREAD_COUNT_ATTR_NAME = "TotalStarted" + THREAD_COUNT_ATTR_NAME;

    public final static String[] ATTR_NAMES = ArrayUtils.toArray(TOTAL_STARTED_THREAD_COUNT_ATTR_NAME);

    private Map<Long, EsacThreadInfo> threadInfos;
    private Map<State, Integer> stateThreadCounts;
    private int nativeThreadCount;
    private int deadlockedThreadCount;
    private int monitorDeadlockedThreadCount;
    private int synchronizerDeadlockedThreadCount;
    private int daemonThreadCount;
    private int threadCount;
    private long totalStartedThreadCount;

    public ThreadMxBeanSnapshotImpl(AttributeList attrs, Map<Long, EsacThreadInfo> threadInfos, Map<State, Integer> stateThreadCounts,
        @Nonnegative int nativeThreadCount, @Nonnegative int deadlockedThreadCount, @Nonnegative int monitorDeadlockedThreadCount,
        @Nonnegative int synchronizerDeadlockedThreadCount) {
        super(attrs);

        this.threadInfos = threadInfos;
        this.stateThreadCounts = stateThreadCounts;
        this.nativeThreadCount = nativeThreadCount;
        this.deadlockedThreadCount = deadlockedThreadCount;
        this.monitorDeadlockedThreadCount = monitorDeadlockedThreadCount;
        this.synchronizerDeadlockedThreadCount = synchronizerDeadlockedThreadCount;
    }

    @Override
    public void resetPeakThreadCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long[] findMonitorDeadlockedThreads() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long[] findDeadlockedThreads() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long[] getThreadUserTime(long[] ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long[] getThreadCpuTime(long[] ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getThreadUserTime(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getThreadCpuTime(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long[] getThreadAllocatedBytes(long[] ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getThreadAllocatedBytes(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ThreadInfo[] getThreadInfo(long[] ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ThreadInfo[] getThreadInfo(long[] ids, int maxDepth) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ThreadInfo[] getThreadInfo(long[] ids, boolean lockedMonitors, boolean lockedSynchronizers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ThreadInfo getThreadInfo(long id, int maxDepth) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ThreadInfo getThreadInfo(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ThreadInfo[] dumpAllThreads(boolean lockedMonitors, boolean lockedSynchronizers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long[] getAllThreadIds() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void buildAttributes(Map<String, Object> attrs) {
        // noinspection ConstantConditions
        this.daemonThreadCount = EsacMapUtils.getOrDefault(attrs, DAEMON_THREAD_COUNT_ATTR_NAME, 0);
        // noinspection ConstantConditions
        this.threadCount = EsacMapUtils.getOrDefault(attrs, THREAD_COUNT_ATTR_NAME, 0);
        // noinspection ConstantConditions
        this.totalStartedThreadCount = EsacMapUtils.getOrDefault(attrs, TOTAL_STARTED_THREAD_COUNT_ATTR_NAME, 0L);
    }

    @Override
    public String[] getAttributeNames() {
        return ATTR_NAMES;
    }

    @Override
    public long getCurrentThreadCpuTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCurrentThreadCpuTimeSupported() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getCurrentThreadUserTime() {
        throw new UnsupportedOperationException();
    }

    @Nonnegative
    @Override
    public int getDaemonThreadCount() {
        return this.daemonThreadCount;
    }

    @Nonnegative
    @Override
    public int getDeadlockedThreadCount() {
        return this.deadlockedThreadCount;
    }

    @Nonnegative
    @Override
    public int getMonitorDeadlockedThreadCount() {
        return this.monitorDeadlockedThreadCount;
    }

    @Nonnegative
    @Override
    public int getNativeThreadCount() {
        return this.nativeThreadCount;
    }

    @Override
    public boolean isObjectMonitorUsageSupported() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ObjectName getObjectName() {
        return OBJ_NAME;
    }

    @Override
    public int getPeakThreadCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<State, Integer> getStateThreadCounts() {
        return this.stateThreadCounts;
    }

    @Nonnegative
    @Override
    public int getSynchronizerDeadlockedThreadCount() {
        return this.synchronizerDeadlockedThreadCount;
    }

    @Override
    public boolean isSynchronizerUsageSupported() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isThreadAllocatedMemoryEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setThreadAllocatedMemoryEnabled(boolean allocatedMemEnabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isThreadAllocatedMemorySupported() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isThreadContentionMonitoringEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setThreadContentionMonitoringEnabled(boolean contentionMonitoringEnabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isThreadContentionMonitoringSupported() {
        throw new UnsupportedOperationException();
    }

    @Nonnegative
    @Override
    public int getThreadCount() {
        return this.threadCount;
    }

    @Override
    public boolean isThreadCpuTimeEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setThreadCpuTimeEnabled(boolean cpuTimeEnabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isThreadCpuTimeSupported() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Long, EsacThreadInfo> getThreadInfos() {
        return this.threadInfos;
    }

    @Nonnegative
    @Override
    public long getTotalStartedThreadCount() {
        return this.totalStartedThreadCount;
    }
}
