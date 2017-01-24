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

public class SystemManagementMetricSet extends AbstractEsacMetricSet {
    private final static String MANAGEMENT_FILE_DESC_METRIC_NAME_PREFIX = EsacMetricNames.MANAGEMENT_PREFIX + "file.descriptor.";
    private final static String MANAGEMENT_OS_METRIC_NAME_PREFIX = EsacMetricNames.MANAGEMENT_PREFIX + "os.";

    private final static String MANAGEMENT_FILE_DESC_MAX_COUNT_METRIC_NAME =
        MANAGEMENT_FILE_DESC_METRIC_NAME_PREFIX + EsacMetricNames.MAX_SUFFIX + EsacStringUtils.PERIOD + EsacMetricNames.COUNT_SUFFIX;
    private final static String MANAGEMENT_FILE_DESC_OPEN_COUNT_METRIC_NAME = MANAGEMENT_FILE_DESC_METRIC_NAME_PREFIX + "open." + EsacMetricNames.COUNT_SUFFIX;
    private final static String MANAGEMENT_OS_ARCH_METRIC_NAME = MANAGEMENT_OS_METRIC_NAME_PREFIX + "arch";
    private final static String MANAGEMENT_OS_NAME_METRIC_NAME = MANAGEMENT_OS_METRIC_NAME_PREFIX + EsacMetricNames.NAME_SUFFIX;
    private final static String MANAGEMENT_OS_VERSION_METRIC_NAME = MANAGEMENT_OS_METRIC_NAME_PREFIX + EsacMetricNames.VERSION_SUFFIX;

    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(MANAGEMENT_FILE_DESC_MAX_COUNT_METRIC_NAME, ElasticsearchDatatype.LONG),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_FILE_DESC_OPEN_COUNT_METRIC_NAME, ElasticsearchDatatype.LONG),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_OS_ARCH_METRIC_NAME, ElasticsearchDatatype.TEXT),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_OS_NAME_METRIC_NAME, ElasticsearchDatatype.TEXT),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_OS_VERSION_METRIC_NAME, ElasticsearchDatatype.TEXT));

    @Override
    public void initialize(EsacMetricRegistry metricRegistry) {
        metrics.put(MANAGEMENT_FILE_DESC_MAX_COUNT_METRIC_NAME,
            ((Gauge<Long>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getSystemSnapshot().getMaxFileDescriptorCount()));
        metrics.put(MANAGEMENT_FILE_DESC_OPEN_COUNT_METRIC_NAME,
            ((Gauge<Long>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getSystemSnapshot().getOpenFileDescriptorCount()));
        metrics.put(MANAGEMENT_OS_ARCH_METRIC_NAME,
            ((Gauge<String>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getSystemSnapshot().getArch()));
        metrics.put(MANAGEMENT_OS_ARCH_METRIC_NAME,
            ((Gauge<String>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getSystemSnapshot().getName()));
        metrics.put(MANAGEMENT_OS_ARCH_METRIC_NAME,
            ((Gauge<String>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getSystemSnapshot().getVersion()));
    }

    @Nullable
    @Override
    public ElasticsearchFieldMapping[] getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
