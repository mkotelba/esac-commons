package com.esacinc.commons.management;

public interface ManagementSnapshot {
    public ClassLoadingMxBeanSnapshot getClassLoadingSnapshot();

    public JvmMxBeanSnapshot getJvmSnapshot();

    public MemoryMxBeanSnapshot getMemorySnapshot();

    public SystemMxBeanSnapshot getSystemSnapshot();

    public ThreadMxBeanSnapshot getThreadSnapshot();
}
