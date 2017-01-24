package com.esacinc.commons.logging.metrics;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapper;
import com.esacinc.commons.logging.metrics.impl.EsacMetricRegistry;
import java.util.SortedMap;

public interface EsacMetricSet extends ElasticsearchFieldMapper, MetricSet {
    public void initialize(EsacMetricRegistry metricRegistry);

    @Override
    public SortedMap<String, Metric> getMetrics();
}
