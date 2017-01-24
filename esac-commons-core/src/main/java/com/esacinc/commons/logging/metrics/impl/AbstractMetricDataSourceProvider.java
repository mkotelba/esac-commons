package com.esacinc.commons.logging.metrics.impl;

import com.esacinc.commons.logging.metrics.MetricDataSourceProvider;

public abstract class AbstractMetricDataSourceProvider<T> implements MetricDataSourceProvider<T> {
    protected Class<T> dataSrcClass;

    protected AbstractMetricDataSourceProvider(Class<T> dataSrcClass) {
        this.dataSrcClass = dataSrcClass;
    }

    @Override
    public Class<T> getDataSourceClass() {
        return this.dataSrcClass;
    }
}
