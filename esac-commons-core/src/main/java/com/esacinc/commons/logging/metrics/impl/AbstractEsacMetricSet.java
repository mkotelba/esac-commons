package com.esacinc.commons.logging.metrics.impl;

import com.codahale.metrics.Metric;
import com.esacinc.commons.config.property.impl.PropertyNameComparator;
import com.esacinc.commons.logging.metrics.EsacMetricSet;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class AbstractEsacMetricSet implements EsacMetricSet {
    protected SortedMap<String, Metric> metrics = new TreeMap<>(PropertyNameComparator.INSTANCE);
    
    @Override
    public SortedMap<String, Metric> getMetrics() {
        return this.metrics;
    }
}
