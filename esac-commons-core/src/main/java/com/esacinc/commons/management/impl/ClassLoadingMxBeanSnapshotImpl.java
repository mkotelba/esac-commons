package com.esacinc.commons.management.impl;

import com.esacinc.commons.utils.EsacMapUtils;
import com.esacinc.commons.management.ClassLoadingMxBeanSnapshot;
import com.esacinc.commons.management.utils.EsacManagementUtils;
import java.lang.management.ManagementFactory;
import java.util.Map;
import javax.annotation.Nonnegative;
import javax.management.AttributeList;
import javax.management.ObjectName;
import org.apache.commons.lang3.ArrayUtils;

public class ClassLoadingMxBeanSnapshotImpl extends AbstractMxBeanSnapshot implements ClassLoadingMxBeanSnapshot {
    public final static ObjectName OBJ_NAME = EsacManagementUtils.buildObjectName(ManagementFactory.CLASS_LOADING_MXBEAN_NAME);

    public final static String CLASS_COUNT_ATTR_NAME_SUFFIX = "Class" + COUNT_ATTR_NAME;

    public final static String LOADED_CLASS_COUNT_ATTR_NAME = "Loaded" + CLASS_COUNT_ATTR_NAME_SUFFIX;
    public final static String TOTAL_LOADED_CLASS_COUNT_ATTR_NAME = TOTAL_ATTR_NAME_PREFIX + LOADED_CLASS_COUNT_ATTR_NAME;
    public final static String UNLOADED_CLASS_COUNT_ATTR_NAME = "Unloaded" + CLASS_COUNT_ATTR_NAME_SUFFIX;

    public final static String[] ATTR_NAMES =
        ArrayUtils.toArray(LOADED_CLASS_COUNT_ATTR_NAME, TOTAL_LOADED_CLASS_COUNT_ATTR_NAME, UNLOADED_CLASS_COUNT_ATTR_NAME);

    private int loadedClassCount;
    private long totalLoadedClassCount;
    private long unloadedClassCount;

    public ClassLoadingMxBeanSnapshotImpl(AttributeList attrs) {
        super(attrs);
    }

    @Override
    protected void buildAttributes(Map<String, Object> attrs) {
        // noinspection ConstantConditions
        this.loadedClassCount = EsacMapUtils.getOrDefault(attrs, LOADED_CLASS_COUNT_ATTR_NAME, 0);
        // noinspection ConstantConditions
        this.totalLoadedClassCount = EsacMapUtils.getOrDefault(attrs, TOTAL_LOADED_CLASS_COUNT_ATTR_NAME, 0L);
        // noinspection ConstantConditions
        this.unloadedClassCount = EsacMapUtils.getOrDefault(attrs, UNLOADED_CLASS_COUNT_ATTR_NAME, 0L);
    }

    @Override
    public String[] getAttributeNames() {
        return ATTR_NAMES;
    }

    @Nonnegative
    @Override
    public int getLoadedClassCount() {
        return this.loadedClassCount;
    }

    @Override
    public ObjectName getObjectName() {
        return OBJ_NAME;
    }

    @Nonnegative
    @Override
    public long getTotalLoadedClassCount() {
        return this.totalLoadedClassCount;
    }

    @Nonnegative
    @Override
    public long getUnloadedClassCount() {
        return this.unloadedClassCount;
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
