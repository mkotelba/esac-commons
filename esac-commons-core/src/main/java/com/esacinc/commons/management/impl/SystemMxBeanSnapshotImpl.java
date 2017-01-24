package com.esacinc.commons.management.impl;

import com.esacinc.commons.utils.EsacMapUtils;
import com.esacinc.commons.management.SystemMxBeanSnapshot;
import com.esacinc.commons.management.utils.EsacManagementUtils;
import java.lang.management.ManagementFactory;
import java.util.Map;
import javax.annotation.Nonnegative;
import javax.management.AttributeList;
import javax.management.ObjectName;
import org.apache.commons.lang3.ArrayUtils;

public class SystemMxBeanSnapshotImpl extends AbstractMxBeanSnapshot implements SystemMxBeanSnapshot {
    public final static ObjectName OBJ_NAME = EsacManagementUtils.buildObjectName(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);

    public final static String CPU_ATTR_NAME_PREFIX = "Cpu";
    public final static String FREE_ATTR_NAME_PREFIX = "Free";
    public final static String PROC_CPU_ATTR_NAME_PREFIX = "Process" + CPU_ATTR_NAME_PREFIX;

    public final static String FILE_DESC_COUNT_ATTR_NAME_SUFFIX = "FileDescriptor" + COUNT_ATTR_NAME;
    public final static String LOAD_ATTR_NAME_SUFFIX = "Load";
    public final static String SIZE_ATTR_NAME_SUFFIX = "Size";
    public final static String MEM_SIZE_ATTR_NAME_SUFFIX = MEM_ATTR_NAME_PREFIX + SIZE_ATTR_NAME_SUFFIX;
    public final static String PHYSICAL_MEM_SIZE_ATTR_NAME_SUFFIX = "Physical" + MEM_SIZE_ATTR_NAME_SUFFIX;
    public final static String SWAP_SPACE_SIZE_ATTR_NAME_SUFFIX = "SwapSpace" + SIZE_ATTR_NAME_SUFFIX;

    public final static String ARCH_ATTR_NAME = "Arch";
    public final static String AVAILABLE_PROCS_ATTR_NAME = "AvailableProcessors";
    public final static String COMMITTED_VIRTUAL_MEM_SIZE_ATTR_NAME = "CommittedVirtual" + MEM_SIZE_ATTR_NAME_SUFFIX;
    public final static String FREE_PHYSICAL_MEM_SIZE_ATTR_NAME = FREE_ATTR_NAME_PREFIX + PHYSICAL_MEM_SIZE_ATTR_NAME_SUFFIX;
    public final static String FREE_SWAP_SPACE_SIZE_ATTR_NAME = FREE_ATTR_NAME_PREFIX + SWAP_SPACE_SIZE_ATTR_NAME_SUFFIX;
    public final static String MAX_FILE_DESC_COUNT_ATTR_NAME = "Max" + FILE_DESC_COUNT_ATTR_NAME_SUFFIX;
    public final static String OPEN_FILE_DESC_COUNT_ATTR_NAME = "Open" + FILE_DESC_COUNT_ATTR_NAME_SUFFIX;
    public final static String PROC_CPU_LOAD_ATTR_NAME = PROC_CPU_ATTR_NAME_PREFIX + LOAD_ATTR_NAME_SUFFIX;
    public final static String PROC_CPU_TIME_ATTR_NAME = PROC_CPU_ATTR_NAME_PREFIX + TIME_ATTR_NAME_SUFFIX;
    public final static String SYS_CPU_LOAD_ATTR_NAME = "System" + CPU_ATTR_NAME_PREFIX + LOAD_ATTR_NAME_SUFFIX;
    public final static String TOTAL_PHYSICAL_MEM_SIZE_ATTR_NAME = TOTAL_ATTR_NAME_PREFIX + PHYSICAL_MEM_SIZE_ATTR_NAME_SUFFIX;
    public final static String TOTAL_SWAP_SPACE_SIZE_ATTR_NAME = TOTAL_ATTR_NAME_PREFIX + SWAP_SPACE_SIZE_ATTR_NAME_SUFFIX;

    public final static String[] ATTR_NAMES = ArrayUtils.toArray(ARCH_ATTR_NAME, AVAILABLE_PROCS_ATTR_NAME, COMMITTED_VIRTUAL_MEM_SIZE_ATTR_NAME,
        FREE_PHYSICAL_MEM_SIZE_ATTR_NAME, FREE_SWAP_SPACE_SIZE_ATTR_NAME, NAME_ATTR_NAME, PROC_CPU_LOAD_ATTR_NAME, PROC_CPU_TIME_ATTR_NAME,
        SYS_CPU_LOAD_ATTR_NAME, TOTAL_PHYSICAL_MEM_SIZE_ATTR_NAME, TOTAL_SWAP_SPACE_SIZE_ATTR_NAME, VERSION_ATTR_NAME);

    public final static String[] UNIX_ATTR_NAMES = ArrayUtils.addAll(ATTR_NAMES, MAX_FILE_DESC_COUNT_ATTR_NAME, OPEN_FILE_DESC_COUNT_ATTR_NAME);

    private String arch;
    private int availableProcs;
    private long committedVirtualMemSize;
    private long freePhysicalMemSize;
    private long freeSwapSpaceSize;
    private long maxFileDescCount;
    private String name;
    private long openFileDescCount;
    private double procCpuLoad;
    private long procCpuTime;
    private double sysCpuLoad;
    private long totalPhysicalMemSize;
    private long totalSwapSpaceSize;
    private String version;

    public SystemMxBeanSnapshotImpl(AttributeList attrs) {
        super(attrs);
    }

    @Override
    protected void buildAttributes(Map<String, Object> attrs) {
        this.arch = EsacMapUtils.getOrDefault(attrs, ARCH_ATTR_NAME);
        // noinspection ConstantConditions
        this.availableProcs = EsacMapUtils.getOrDefault(attrs, AVAILABLE_PROCS_ATTR_NAME, 0);
        // noinspection ConstantConditions
        this.committedVirtualMemSize = EsacMapUtils.getOrDefault(attrs, COMMITTED_VIRTUAL_MEM_SIZE_ATTR_NAME, 0L);
        // noinspection ConstantConditions
        this.freePhysicalMemSize = EsacMapUtils.getOrDefault(attrs, FREE_PHYSICAL_MEM_SIZE_ATTR_NAME, 0L);
        // noinspection ConstantConditions
        this.freeSwapSpaceSize = EsacMapUtils.getOrDefault(attrs, FREE_SWAP_SPACE_SIZE_ATTR_NAME, 0L);
        // noinspection ConstantConditions
        this.maxFileDescCount = EsacMapUtils.getOrDefault(attrs, MAX_FILE_DESC_COUNT_ATTR_NAME, -1L);
        this.name = EsacMapUtils.getOrDefault(attrs, NAME_ATTR_NAME);
        // noinspection ConstantConditions
        this.openFileDescCount = EsacMapUtils.getOrDefault(attrs, OPEN_FILE_DESC_COUNT_ATTR_NAME, -1L);
        // noinspection ConstantConditions
        this.procCpuLoad = EsacMapUtils.getOrDefault(attrs, PROC_CPU_LOAD_ATTR_NAME, 0D);
        // noinspection ConstantConditions
        this.procCpuTime = EsacMapUtils.getOrDefault(attrs, PROC_CPU_TIME_ATTR_NAME, 0L);
        // noinspection ConstantConditions
        this.sysCpuLoad = EsacMapUtils.getOrDefault(attrs, SYS_CPU_LOAD_ATTR_NAME, 0D);
        // noinspection ConstantConditions
        this.totalPhysicalMemSize = EsacMapUtils.getOrDefault(attrs, TOTAL_PHYSICAL_MEM_SIZE_ATTR_NAME, 0L);
        // noinspection ConstantConditions
        this.totalSwapSpaceSize = EsacMapUtils.getOrDefault(attrs, TOTAL_SWAP_SPACE_SIZE_ATTR_NAME, 0L);
        this.version = EsacMapUtils.getOrDefault(attrs, VERSION_ATTR_NAME);
    }

    @Override
    public String getArch() {
        return this.arch;
    }

    @Override
    public String[] getAttributeNames() {
        return ATTR_NAMES;
    }

    @Nonnegative
    @Override
    public int getAvailableProcessors() {
        return this.availableProcs;
    }

    @Nonnegative
    @Override
    public long getCommittedVirtualMemorySize() {
        return this.committedVirtualMemSize;
    }

    @Nonnegative
    @Override
    public long getFreePhysicalMemorySize() {
        return this.freePhysicalMemSize;
    }

    @Nonnegative
    @Override
    public long getFreeSwapSpaceSize() {
        return this.freeSwapSpaceSize;
    }

    @Override
    public long getMaxFileDescriptorCount() {
        return this.maxFileDescCount;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ObjectName getObjectName() {
        return OBJ_NAME;
    }

    @Override
    public long getOpenFileDescriptorCount() {
        return this.openFileDescCount;
    }

    @Nonnegative
    @Override
    public double getProcessCpuLoad() {
        return this.procCpuLoad;
    }

    @Nonnegative
    @Override
    public long getProcessCpuTime() {
        return this.procCpuTime;
    }

    @Nonnegative
    @Override
    public double getSystemCpuLoad() {
        return this.sysCpuLoad;
    }

    @Nonnegative
    @Override
    public double getSystemLoadAverage() {
        throw new UnsupportedOperationException();
    }

    @Nonnegative
    @Override
    public long getTotalPhysicalMemorySize() {
        return this.totalPhysicalMemSize;
    }

    @Nonnegative
    @Override
    public long getTotalSwapSpaceSize() {
        return this.totalSwapSpaceSize;
    }

    @Override
    public String[] getUnixAttributeNames() {
        return UNIX_ATTR_NAMES;
    }

    @Override
    public String getVersion() {
        return this.version;
    }
}
