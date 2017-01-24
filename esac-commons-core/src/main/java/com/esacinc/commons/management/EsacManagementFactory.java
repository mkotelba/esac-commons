package com.esacinc.commons.management;

import com.esacinc.commons.logging.metrics.MetricDataSourceProvider;
import org.springframework.beans.factory.InitializingBean;

public interface EsacManagementFactory extends InitializingBean, MetricDataSourceProvider<ManagementSnapshot> {
}
