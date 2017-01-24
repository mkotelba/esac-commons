package com.esacinc.commons.management.impl;

import com.esacinc.commons.management.MemoryPoolMxBeanSnapshot;
import com.esacinc.commons.management.utils.EsacManagementUtils;
import com.esacinc.commons.utils.EsacMapUtils;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.util.Map;
import javax.management.AttributeList;
import javax.management.ObjectName;
import org.apache.commons.lang3.ArrayUtils;

public class MemoryPoolMxBeanSnapshotImpl extends AbstractPoolMxBeanSnapshot implements MemoryPoolMxBeanSnapshot {
    public final static String TYPE_ATTR_NAME = "Type";

    public final static String[] ATTR_NAMES = ArrayUtils.toArray(TYPE_ATTR_NAME, USAGE_ATTR_NAME);

    private MemoryType type;
    private MemoryPoolUsage usage;

    public MemoryPoolMxBeanSnapshotImpl(String name, ObjectName objName, AttributeList attrs) {
        super(name, objName, attrs);
    }

    @Override
    public void resetPeakUsage() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void buildAttributes(Map<String, Object> attrs) {
        this.type = MemoryType.valueOf(EsacMapUtils.getOrDefault(attrs, TYPE_ATTR_NAME));
        this.usage = new MemoryPoolUsage(EsacManagementUtils.mapData(EsacMapUtils.getOrDefault(attrs, USAGE_ATTR_NAME)));
    }

    @Override
    public String[] getAttributeNames() {
        return ATTR_NAMES;
    }

    @Override
    public MemoryUsage getCollectionUsage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getCollectionUsageThreshold() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCollectionUsageThreshold(long threshold) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getCollectionUsageThresholdCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCollectionUsageThresholdExceeded() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCollectionUsageThresholdSupported() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getMemoryManagerNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MemoryUsage getPeakUsage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MemoryType getType() {
        return this.type;
    }

    @Override
    public MemoryPoolUsage getUsage() {
        return this.usage;
    }

    @Override
    public long getUsageThreshold() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setUsageThreshold(long threshold) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getUsageThresholdCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isUsageThresholdExceeded() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isUsageThresholdSupported() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isValid() {
        throw new UnsupportedOperationException();
    }
}
