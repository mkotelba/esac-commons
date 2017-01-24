package com.esacinc.commons.management.impl;

import com.esacinc.commons.management.ClassLoadingMxBeanSnapshot;
import com.esacinc.commons.management.JvmMxBeanSnapshot;
import com.esacinc.commons.management.ManagementSnapshot;
import com.esacinc.commons.management.MemoryMxBeanSnapshot;
import com.esacinc.commons.management.SystemMxBeanSnapshot;
import com.esacinc.commons.management.ThreadMxBeanSnapshot;

public class ManagementSnapshotImpl implements ManagementSnapshot {
    private ClassLoadingMxBeanSnapshot classLoadingSnapshot;
    private JvmMxBeanSnapshot jvmSnapshot;
    private MemoryMxBeanSnapshot memSnapshot;
    private SystemMxBeanSnapshot sysSnapshot;
    private ThreadMxBeanSnapshot threadSnapshot;

    public ManagementSnapshotImpl(ClassLoadingMxBeanSnapshot classLoadingSnapshot, JvmMxBeanSnapshot jvmSnapshot, MemoryMxBeanSnapshot memSnapshot,
        SystemMxBeanSnapshot sysSnapshot, ThreadMxBeanSnapshot threadSnapshot) {
        this.classLoadingSnapshot = classLoadingSnapshot;
        this.jvmSnapshot = jvmSnapshot;
        this.memSnapshot = memSnapshot;
        this.sysSnapshot = sysSnapshot;
        this.threadSnapshot = threadSnapshot;
    }

    @Override
    public ClassLoadingMxBeanSnapshot getClassLoadingSnapshot() {
        return this.classLoadingSnapshot;
    }

    @Override
    public JvmMxBeanSnapshot getJvmSnapshot() {
        return this.jvmSnapshot;
    }

    @Override
    public MemoryMxBeanSnapshot getMemorySnapshot() {
        return this.memSnapshot;
    }

    @Override
    public SystemMxBeanSnapshot getSystemSnapshot() {
        return this.sysSnapshot;
    }

    @Override
    public ThreadMxBeanSnapshot getThreadSnapshot() {
        return this.threadSnapshot;
    }
}
