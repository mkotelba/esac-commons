package com.esacinc.commons.management.logging.metrics.impl;

import com.codahale.metrics.Gauge;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import com.esacinc.commons.logging.metrics.EsacMetricNames;
import com.esacinc.commons.logging.metrics.impl.AbstractEsacMetricSet;
import com.esacinc.commons.logging.metrics.impl.EsacMetricRegistry;
import com.esacinc.commons.management.ManagementSnapshot;
import com.esacinc.commons.management.MemoryMxBeanSnapshot;
import com.esacinc.commons.management.SystemMxBeanSnapshot;
import com.esacinc.commons.management.impl.EsacMemoryUsage;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;

public class MemoryManagementMetricSet extends AbstractEsacMetricSet {
    private final static String MANAGEMENT_MEMORY_METRIC_NAME = EsacMetricNames.MANAGEMENT_PREFIX + "memory";
    private final static String MANAGEMENT_MEMORY_PHYSICAL_METRIC_NAME = MANAGEMENT_MEMORY_METRIC_NAME + ".physical";
    private final static String MANAGEMENT_MEMORY_SWAP_METRIC_NAME = MANAGEMENT_MEMORY_METRIC_NAME + ".swap";
    private final static String MANAGEMENT_MEMORY_VIRTUAL_COMMITTED_METRIC_NAME = MANAGEMENT_MEMORY_METRIC_NAME + ".virtual.committed";

    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(MANAGEMENT_MEMORY_METRIC_NAME, ElasticsearchDatatype.OBJECT, MemoryMxBeanSnapshot.class),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_MEMORY_PHYSICAL_METRIC_NAME, ElasticsearchDatatype.OBJECT, EsacMemoryUsage.class),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_MEMORY_SWAP_METRIC_NAME, ElasticsearchDatatype.OBJECT, EsacMemoryUsage.class),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_MEMORY_VIRTUAL_COMMITTED_METRIC_NAME, ElasticsearchDatatype.LONG));

    @Override
    public void initialize(EsacMetricRegistry metricRegistry) {
        metrics.put(MANAGEMENT_MEMORY_METRIC_NAME,
            ((Gauge<MemoryMxBeanSnapshot>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getMemorySnapshot()));
        metrics.put(MANAGEMENT_MEMORY_PHYSICAL_METRIC_NAME, ((Gauge<EsacMemoryUsage>) () -> {
            SystemMxBeanSnapshot sysSnapshot = metricRegistry.getDataSource(ManagementSnapshot.class).getSystemSnapshot();

            return new EsacMemoryUsage(0L, sysSnapshot.getTotalPhysicalMemorySize(), sysSnapshot.getFreePhysicalMemorySize());
        }));
        metrics.put(MANAGEMENT_MEMORY_SWAP_METRIC_NAME, ((Gauge<EsacMemoryUsage>) () -> {
            SystemMxBeanSnapshot sysSnapshot = metricRegistry.getDataSource(ManagementSnapshot.class).getSystemSnapshot();

            return new EsacMemoryUsage(0L, sysSnapshot.getTotalSwapSpaceSize(), sysSnapshot.getFreeSwapSpaceSize());
        }));
        metrics.put(MANAGEMENT_MEMORY_VIRTUAL_COMMITTED_METRIC_NAME,
            ((Gauge<Long>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getSystemSnapshot().getCommittedVirtualMemorySize()));
    }

    @Nullable
    @Override
    public ElasticsearchFieldMapping[] getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
