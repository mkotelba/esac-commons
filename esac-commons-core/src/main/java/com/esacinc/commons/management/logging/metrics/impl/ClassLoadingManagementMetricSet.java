package com.esacinc.commons.management.logging.metrics.impl;

import com.codahale.metrics.Gauge;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import com.esacinc.commons.logging.metrics.EsacMetricNames;
import com.esacinc.commons.logging.metrics.impl.AbstractEsacMetricSet;
import com.esacinc.commons.logging.metrics.impl.EsacMetricRegistry;
import com.esacinc.commons.management.ManagementSnapshot;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;

public class ClassLoadingManagementMetricSet extends AbstractEsacMetricSet {
    private final static String MANAGEMENT_CLASSES_METRIC_NAME_PREFIX = EsacMetricNames.MANAGEMENT_PREFIX + "classes.";

    private final static String MANAGEMENT_CLASSES_LOADED_NAME_METRIC_NAME = MANAGEMENT_CLASSES_METRIC_NAME_PREFIX + "loaded";

    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(MANAGEMENT_CLASSES_LOADED_NAME_METRIC_NAME, ElasticsearchDatatype.INTEGER));

    @Override
    public void initialize(EsacMetricRegistry metricRegistry) {
        metrics.put(MANAGEMENT_CLASSES_LOADED_NAME_METRIC_NAME,
            ((Gauge<Integer>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getClassLoadingSnapshot().getLoadedClassCount()));
    }

    @Nullable
    @Override
    public ElasticsearchFieldMapping[] getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
