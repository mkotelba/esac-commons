package com.esacinc.commons.management.impl;

import com.esacinc.commons.utils.EsacMapUtils;
import com.esacinc.commons.management.BufferPoolMxBeanSnapshot;
import com.esacinc.commons.management.MemoryMxBeanSnapshot;
import com.esacinc.commons.management.MemoryPoolMxBeanSnapshot;
import com.esacinc.commons.management.utils.EsacManagementUtils;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import javax.management.AttributeList;
import javax.management.ObjectName;
import org.apache.commons.lang3.ArrayUtils;

public class MemoryMxBeanSnapshotImpl extends AbstractMxBeanSnapshot implements MemoryMxBeanSnapshot {
    public final static ObjectName OBJ_NAME = EsacManagementUtils.buildObjectName(ManagementFactory.MEMORY_MXBEAN_NAME);

    public final static String HEAP_MEM_USAGE_ATTR_NAME = "Heap" + MEM_ATTR_NAME_PREFIX + USAGE_ATTR_NAME;
    public final static String NON_HEAP_MEM_USAGE_ATTR_NAME = "Non" + HEAP_MEM_USAGE_ATTR_NAME;
    public final static String OBJ_PENDING_FINALIZATION_COUNT_ATTR_NAME = "ObjectPendingFinalization" + COUNT_ATTR_NAME;

    public final static String[] ATTR_NAMES =
        ArrayUtils.toArray(HEAP_MEM_USAGE_ATTR_NAME, NON_HEAP_MEM_USAGE_ATTR_NAME, OBJ_PENDING_FINALIZATION_COUNT_ATTR_NAME);

    private List<MemoryPoolMxBeanSnapshot> poolSnapshots;
    private List<BufferPoolMxBeanSnapshot> bufferPoolSnapshots;
    private EsacMemoryUsage heapMemUsage;
    private EsacMemoryUsage nonHeapMemUsage;
    private int objPendingFinalizationCount;

    public MemoryMxBeanSnapshotImpl(AttributeList attrs, List<MemoryPoolMxBeanSnapshot> poolSnapshots, List<BufferPoolMxBeanSnapshot> bufferPoolSnapshots) {
        super(attrs);

        this.poolSnapshots = poolSnapshots;
        this.bufferPoolSnapshots = bufferPoolSnapshots;
    }

    @Override
    public void gc() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void buildAttributes(Map<String, Object> attrs) {
        this.heapMemUsage = new EsacMemoryUsage(EsacManagementUtils.mapData(EsacMapUtils.getOrDefault(attrs, HEAP_MEM_USAGE_ATTR_NAME)));
        this.nonHeapMemUsage = new EsacMemoryUsage(EsacManagementUtils.mapData(EsacMapUtils.getOrDefault(attrs, NON_HEAP_MEM_USAGE_ATTR_NAME)));
        // noinspection ConstantConditions
        this.objPendingFinalizationCount = EsacMapUtils.getOrDefault(attrs, OBJ_PENDING_FINALIZATION_COUNT_ATTR_NAME, 0);
    }

    @Override
    public String[] getAttributeNames() {
        return ATTR_NAMES;
    }

    @Override
    public List<BufferPoolMxBeanSnapshot> getBufferPoolSnapshots() {
        return this.bufferPoolSnapshots;
    }

    @Override
    public EsacMemoryUsage getHeapMemoryUsage() {
        return this.heapMemUsage;
    }

    @Override
    public EsacMemoryUsage getNonHeapMemoryUsage() {
        return this.nonHeapMemUsage;
    }

    @Override
    public ObjectName getObjectName() {
        return OBJ_NAME;
    }

    @Override
    public int getObjectPendingFinalizationCount() {
        return this.objPendingFinalizationCount;
    }

    @Override
    public List<MemoryPoolMxBeanSnapshot> getPoolSnapshots() {
        return this.poolSnapshots;
    }

    @Override
    public boolean isVerbose() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVerbose(boolean verbose) {
        throw new UnsupportedOperationException();
    }
}
