package com.esacinc.commons.logging.metrics;

public interface MetricDataSourceProvider<T> {
    public T buildDataSource(boolean refresh);

    public Class<T> getDataSourceClass();
}
