package com.esacinc.commons.management.impl;

import com.esacinc.commons.utils.EsacMapUtils;
import com.esacinc.commons.management.BufferPoolMxBeanSnapshot;
import java.util.Map;
import javax.annotation.Nonnegative;
import javax.management.AttributeList;
import javax.management.ObjectName;
import org.apache.commons.lang3.ArrayUtils;

public class BufferPoolMxBeanSnapshotImpl extends AbstractPoolMxBeanSnapshot implements BufferPoolMxBeanSnapshot {
    public final static String OBJ_NAME_DOMAIN_TYPE = "java.nio:type=BufferPool";

    public final static String MEM_USED_ATTR_NAME = MEM_ATTR_NAME_PREFIX + "Used";
    public final static String TOTAL_CAPACITY_ATTR_NAME = TOTAL_ATTR_NAME_PREFIX + "Capacity";

    public final static String[] ATTR_NAMES = ArrayUtils.toArray(COUNT_ATTR_NAME, MEM_USED_ATTR_NAME, TOTAL_CAPACITY_ATTR_NAME);

    private long count;
    private long memUsed;
    private long totalCapacity;
    private EsacMemoryUsage usage;

    public BufferPoolMxBeanSnapshotImpl(String name, ObjectName objName, AttributeList attrs) {
        super(name, objName, attrs);
    }

    @Override
    protected void buildAttributes(Map<String, Object> attrs) {
        // noinspection ConstantConditions
        this.count = EsacMapUtils.getOrDefault(attrs, COUNT_ATTR_NAME, 0L);
        // noinspection ConstantConditions
        this.usage = new EsacMemoryUsage((this.memUsed = EsacMapUtils.getOrDefault(attrs, MEM_USED_ATTR_NAME, 0L)),
            (this.totalCapacity = EsacMapUtils.getOrDefault(attrs, TOTAL_CAPACITY_ATTR_NAME, 0L)), -1L);
    }

    @Override
    public String[] getAttributeNames() {
        return ATTR_NAMES;
    }

    @Nonnegative
    @Override
    public long getCount() {
        return this.count;
    }

    @Nonnegative
    @Override
    public long getMemoryUsed() {
        return this.memUsed;
    }

    @Nonnegative
    @Override
    public long getTotalCapacity() {
        return this.totalCapacity;
    }

    @Override
    public EsacMemoryUsage getUsage() {
        return this.usage;
    }
}
