package com.esacinc.commons.management.impl;

import com.esacinc.commons.utils.EsacStringUtils;
import com.esacinc.commons.management.JvmMxBeanSnapshot;
import com.esacinc.commons.management.utils.EsacManagementUtils;
import com.esacinc.commons.utils.EsacMapUtils;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnegative;
import javax.management.AttributeList;
import javax.management.ObjectName;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class JvmMxBeanSnapshotImpl extends AbstractMxBeanSnapshot implements JvmMxBeanSnapshot {
    public final static ObjectName OBJ_NAME = EsacManagementUtils.buildObjectName(ManagementFactory.RUNTIME_MXBEAN_NAME);

    public final static String SPEC_ATTR_NAME_PREFIX = "Spec";
    public final static String VM_ATTR_NAME_PREFIX = "Vm";

    public final static String VENDOR_ATTR_NAME_SUFFIX = "Vendor";

    public final static String SPEC_NAME_ATTR_NAME = SPEC_ATTR_NAME_PREFIX + NAME_ATTR_NAME;
    public final static String SPEC_VENDOR_ATTR_NAME = SPEC_ATTR_NAME_PREFIX + VENDOR_ATTR_NAME_SUFFIX;
    public final static String SPEC_VERSION_ATTR_NAME = SPEC_ATTR_NAME_PREFIX + VERSION_ATTR_NAME;
    public final static String MANAGEMENT_SPEC_VERSION_ATTR_NAME = "Management" + SPEC_VERSION_ATTR_NAME;
    public final static String START_TIME_ATTR_NAME = "Start" + TIME_ATTR_NAME_SUFFIX;
    public final static String UPTIME_ATTR_NAME = "Uptime";
    public final static String VM_NAME_ATTR_NAME = VM_ATTR_NAME_PREFIX + NAME_ATTR_NAME;
    public final static String VM_VENDOR_ATTR_NAME = VM_ATTR_NAME_PREFIX + VENDOR_ATTR_NAME_SUFFIX;
    public final static String VM_VERSION_ATTR_NAME = VM_ATTR_NAME_PREFIX + VERSION_ATTR_NAME;

    public final static String[] ATTR_NAMES = ArrayUtils.toArray(MANAGEMENT_SPEC_VERSION_ATTR_NAME, NAME_ATTR_NAME, SPEC_NAME_ATTR_NAME, SPEC_VENDOR_ATTR_NAME,
        SPEC_VERSION_ATTR_NAME, START_TIME_ATTR_NAME, UPTIME_ATTR_NAME, VM_NAME_ATTR_NAME, VM_VENDOR_ATTR_NAME, VM_VERSION_ATTR_NAME);

    private String hostName;
    private String managementSpecVersion;
    private long pid;
    private String specName;
    private String specVendor;
    private String specVersion;
    private long startTime;
    private long uptime;
    private String vmName;
    private String vmVendor;
    private String vmVersion;

    public JvmMxBeanSnapshotImpl(AttributeList attrs) {
        super(attrs);
    }

    @Override
    protected void buildAttributes(Map<String, Object> attrs) {
        this.managementSpecVersion = EsacMapUtils.getOrDefault(attrs, MANAGEMENT_SPEC_VERSION_ATTR_NAME);
        this.specName = EsacMapUtils.getOrDefault(attrs, SPEC_NAME_ATTR_NAME);
        this.specVendor = EsacMapUtils.getOrDefault(attrs, SPEC_VENDOR_ATTR_NAME);
        this.specVersion = EsacMapUtils.getOrDefault(attrs, SPEC_VERSION_ATTR_NAME);
        // noinspection ConstantConditions
        this.startTime = EsacMapUtils.getOrDefault(attrs, START_TIME_ATTR_NAME, 0L);
        // noinspection ConstantConditions
        this.uptime = EsacMapUtils.getOrDefault(attrs, UPTIME_ATTR_NAME, 0L);
        this.vmName = EsacMapUtils.getOrDefault(attrs, VM_NAME_ATTR_NAME);
        this.vmVendor = EsacMapUtils.getOrDefault(attrs, VM_VENDOR_ATTR_NAME);
        this.vmVersion = EsacMapUtils.getOrDefault(attrs, VM_VERSION_ATTR_NAME);

        String[] nameParts = StringUtils.split(EsacMapUtils.getOrDefault(attrs, NAME_ATTR_NAME), EsacStringUtils.AMPERSAND, 2);
        this.pid = Long.parseLong(nameParts[0]);
        this.hostName = nameParts[1];
    }

    @Override
    public String[] getAttributeNames() {
        return ATTR_NAMES;
    }

    @Override
    public String getBootClassPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isBootClassPathSupported() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getClassPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getHostName() {
        return this.hostName;
    }

    @Override
    public List<String> getInputArguments() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getLibraryPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getManagementSpecVersion() {
        return this.managementSpecVersion;
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ObjectName getObjectName() {
        return OBJ_NAME;
    }

    @Nonnegative
    @Override
    public long getPid() {
        return this.pid;
    }

    @Override
    public String getSpecName() {
        return this.specName;
    }

    @Override
    public String getSpecVendor() {
        return this.specVendor;
    }

    @Override
    public String getSpecVersion() {
        return this.specVersion;
    }

    @Nonnegative
    @Override
    public long getStartTime() {
        return this.startTime;
    }

    @Override
    public Map<String, String> getSystemProperties() {
        throw new UnsupportedOperationException();
    }

    @Nonnegative
    @Override
    public long getUptime() {
        return this.uptime;
    }

    @Override
    public String getVmName() {
        return this.vmName;
    }

    @Override
    public String getVmVendor() {
        return this.vmVendor;
    }

    @Override
    public String getVmVersion() {
        return this.vmVersion;
    }
}
