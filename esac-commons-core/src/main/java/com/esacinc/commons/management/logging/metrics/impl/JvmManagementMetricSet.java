package com.esacinc.commons.management.logging.metrics.impl;

import com.codahale.metrics.Gauge;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import com.esacinc.commons.logging.metrics.EsacMetricNames;
import com.esacinc.commons.logging.metrics.impl.AbstractEsacMetricSet;
import com.esacinc.commons.logging.metrics.impl.EsacMetricRegistry;
import com.esacinc.commons.management.ManagementSnapshot;
import com.esacinc.commons.utils.EsacStringUtils;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;

public class JvmManagementMetricSet extends AbstractEsacMetricSet {
    private final static String MANAGEMENT_JVM_METRIC_NAME_PREFIX = EsacMetricNames.MANAGEMENT_PREFIX + "jvm.";
    private final static String MANAGEMENT_JVM_SPEC_METRIC_NAME_PREFIX = MANAGEMENT_JVM_METRIC_NAME_PREFIX + EsacMetricNames.SPEC_PREFIX;
    private final static String MANAGEMENT_JVM_TIME_METRIC_NAME_PREFIX = MANAGEMENT_JVM_METRIC_NAME_PREFIX + EsacMetricNames.TIME_SUFFIX + EsacStringUtils.PERIOD;
    private final static String MANAGEMENT_JVM_VM_METRIC_NAME_PREFIX = MANAGEMENT_JVM_METRIC_NAME_PREFIX + "vm.";

    private final static String MANAGEMENT_JVM_SPEC_NAME_METRIC_NAME = MANAGEMENT_JVM_SPEC_METRIC_NAME_PREFIX + EsacMetricNames.NAME_SUFFIX;
    private final static String MANAGEMENT_JVM_SPEC_VENDOR_METRIC_NAME = MANAGEMENT_JVM_SPEC_METRIC_NAME_PREFIX + EsacMetricNames.VENDOR_SUFFIX;
    private final static String MANAGEMENT_JVM_SPEC_VERSION_METRIC_NAME = MANAGEMENT_JVM_SPEC_METRIC_NAME_PREFIX + EsacMetricNames.VERSION_SUFFIX;
    private final static String MANAGEMENT_JVM_TIME_START_METRIC_NAME = MANAGEMENT_JVM_TIME_METRIC_NAME_PREFIX + EsacMetricNames.START_SUFFIX;
    private final static String MANAGEMENT_JVM_TIME_UP_METRIC_NAME = MANAGEMENT_JVM_TIME_METRIC_NAME_PREFIX + "up";
    private final static String MANAGEMENT_JVM_VM_NAME_METRIC_NAME = MANAGEMENT_JVM_VM_METRIC_NAME_PREFIX + EsacMetricNames.NAME_SUFFIX;
    private final static String MANAGEMENT_JVM_VM_VENDOR_METRIC_NAME = MANAGEMENT_JVM_VM_METRIC_NAME_PREFIX + EsacMetricNames.VENDOR_SUFFIX;
    private final static String MANAGEMENT_JVM_VM_VERSION_METRIC_NAME = MANAGEMENT_JVM_VM_METRIC_NAME_PREFIX + EsacMetricNames.VERSION_SUFFIX;
    private final static String MANAGEMENT_SPEC_VERSION_METRIC_NAME = EsacMetricNames.MANAGEMENT_PREFIX + EsacMetricNames.SPEC_PREFIX + EsacMetricNames.VERSION_SUFFIX;

    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(MANAGEMENT_JVM_SPEC_NAME_METRIC_NAME, ElasticsearchDatatype.TEXT),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_JVM_SPEC_VENDOR_METRIC_NAME, ElasticsearchDatatype.TEXT),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_JVM_SPEC_VERSION_METRIC_NAME, ElasticsearchDatatype.TEXT),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_JVM_TIME_START_METRIC_NAME, ElasticsearchDatatype.LONG),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_JVM_TIME_UP_METRIC_NAME, ElasticsearchDatatype.LONG),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_JVM_VM_NAME_METRIC_NAME, ElasticsearchDatatype.TEXT),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_JVM_VM_VENDOR_METRIC_NAME, ElasticsearchDatatype.TEXT),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_JVM_VM_VERSION_METRIC_NAME, ElasticsearchDatatype.TEXT),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_SPEC_VERSION_METRIC_NAME, ElasticsearchDatatype.TEXT));

    @Override
    public void initialize(EsacMetricRegistry metricRegistry) {
        metrics.put(MANAGEMENT_JVM_SPEC_NAME_METRIC_NAME,
            ((Gauge<String>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getJvmSnapshot().getSpecName()));
        metrics.put(MANAGEMENT_JVM_SPEC_VENDOR_METRIC_NAME,
            ((Gauge<String>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getJvmSnapshot().getSpecVendor()));
        metrics.put(MANAGEMENT_JVM_SPEC_VERSION_METRIC_NAME,
            ((Gauge<String>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getJvmSnapshot().getSpecVersion()));
        metrics.put(MANAGEMENT_JVM_TIME_START_METRIC_NAME,
            ((Gauge<Long>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getJvmSnapshot().getStartTime()));
        metrics.put(MANAGEMENT_JVM_TIME_UP_METRIC_NAME,
            ((Gauge<Long>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getJvmSnapshot().getUptime()));
        metrics.put(MANAGEMENT_JVM_VM_NAME_METRIC_NAME,
            ((Gauge<String>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getJvmSnapshot().getVmName()));
        metrics.put(MANAGEMENT_JVM_VM_VENDOR_METRIC_NAME,
            ((Gauge<String>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getJvmSnapshot().getVmVendor()));
        metrics.put(MANAGEMENT_JVM_VM_VERSION_METRIC_NAME,
            ((Gauge<String>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getJvmSnapshot().getVmVersion()));
        metrics.put(MANAGEMENT_SPEC_VERSION_METRIC_NAME,
            ((Gauge<String>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getJvmSnapshot().getManagementSpecVersion()));
    }

    @Nullable
    @Override
    public ElasticsearchFieldMapping[] getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
