package com.esacinc.commons.management.logging.metrics.impl;

import com.codahale.metrics.Gauge;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import com.esacinc.commons.logging.metrics.EsacMetricNames;
import com.esacinc.commons.logging.metrics.impl.AbstractEsacMetricSet;
import com.esacinc.commons.logging.metrics.impl.EsacMetricRegistry;
import com.esacinc.commons.management.ManagementSnapshot;
import java.lang.Thread.State;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;

public class ThreadManagementMetricSet extends AbstractEsacMetricSet {
    private final static String MANAGEMENT_THREAD_METRIC_NAME_PREFIX = EsacMetricNames.MANAGEMENT_PREFIX + "thread.";
    private final static String MANAGEMENT_THREAD_DEADLOCKED_METRIC_NAME_PREFIX = MANAGEMENT_THREAD_METRIC_NAME_PREFIX + "deadlocked.";

    private final static String WAITING_COUNT_METRIC_NAME_SUFFIX = "waiting." + EsacMetricNames.COUNT_SUFFIX;

    private final static String MANAGEMENT_THREAD_BLOCKED_COUNT_METRIC_NAME = MANAGEMENT_THREAD_METRIC_NAME_PREFIX + "blocked." + EsacMetricNames.COUNT_SUFFIX;
    private final static String MANAGEMENT_THREAD_COUNT_METRIC_NAME = MANAGEMENT_THREAD_METRIC_NAME_PREFIX + EsacMetricNames.COUNT_SUFFIX;
    private final static String MANAGEMENT_THREAD_DAEMON_COUNT_METRIC_NAME = MANAGEMENT_THREAD_METRIC_NAME_PREFIX + "daemon." + EsacMetricNames.COUNT_SUFFIX;
    private final static String MANAGEMENT_THREAD_DEADLOCKED_COUNT_METRIC_NAME = MANAGEMENT_THREAD_DEADLOCKED_METRIC_NAME_PREFIX + EsacMetricNames.COUNT_SUFFIX;
    private final static String MANAGEMENT_THREAD_DEADLOCKED_MONITOR_COUNT_METRIC_NAME =
        MANAGEMENT_THREAD_DEADLOCKED_METRIC_NAME_PREFIX + "monitor." + EsacMetricNames.COUNT_SUFFIX;
    private final static String MANAGEMENT_THREAD_DEADLOCKED_SYNCHRONIZER_COUNT_METRIC_NAME =
        MANAGEMENT_THREAD_DEADLOCKED_METRIC_NAME_PREFIX + "synchronizer." + EsacMetricNames.COUNT_SUFFIX;
    private final static String MANAGEMENT_THREAD_NATIVE_COUNT_METRIC_NAME = MANAGEMENT_THREAD_METRIC_NAME_PREFIX + "native." + EsacMetricNames.COUNT_SUFFIX;
    private final static String MANAGEMENT_THREAD_NEW_COUNT_METRIC_NAME = MANAGEMENT_THREAD_METRIC_NAME_PREFIX + "new." + EsacMetricNames.COUNT_SUFFIX;
    private final static String MANAGEMENT_THREAD_RUNNABLE_COUNT_METRIC_NAME = MANAGEMENT_THREAD_METRIC_NAME_PREFIX + "runnable." + EsacMetricNames.COUNT_SUFFIX;
    private final static String MANAGEMENT_THREAD_TERMINATED_COUNT_METRIC_NAME =
        MANAGEMENT_THREAD_METRIC_NAME_PREFIX + "terminated." + EsacMetricNames.COUNT_SUFFIX;
    private final static String MANAGEMENT_THREAD_TIMED_WAITING_COUNT_METRIC_NAME =
        MANAGEMENT_THREAD_METRIC_NAME_PREFIX + "timed_" + WAITING_COUNT_METRIC_NAME_SUFFIX;
    private final static String MANAGEMENT_THREAD_WAITING_COUNT_METRIC_NAME = MANAGEMENT_THREAD_METRIC_NAME_PREFIX + WAITING_COUNT_METRIC_NAME_SUFFIX;

    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(MANAGEMENT_THREAD_BLOCKED_COUNT_METRIC_NAME, ElasticsearchDatatype.INTEGER),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_THREAD_COUNT_METRIC_NAME, ElasticsearchDatatype.INTEGER),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_THREAD_DAEMON_COUNT_METRIC_NAME, ElasticsearchDatatype.INTEGER),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_THREAD_DEADLOCKED_COUNT_METRIC_NAME, ElasticsearchDatatype.INTEGER),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_THREAD_DEADLOCKED_MONITOR_COUNT_METRIC_NAME, ElasticsearchDatatype.INTEGER),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_THREAD_DEADLOCKED_SYNCHRONIZER_COUNT_METRIC_NAME, ElasticsearchDatatype.INTEGER),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_THREAD_NATIVE_COUNT_METRIC_NAME, ElasticsearchDatatype.INTEGER),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_THREAD_NEW_COUNT_METRIC_NAME, ElasticsearchDatatype.INTEGER),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_THREAD_RUNNABLE_COUNT_METRIC_NAME, ElasticsearchDatatype.INTEGER),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_THREAD_TERMINATED_COUNT_METRIC_NAME, ElasticsearchDatatype.INTEGER),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_THREAD_TIMED_WAITING_COUNT_METRIC_NAME, ElasticsearchDatatype.INTEGER),
            new ElasticsearchFieldMappingImpl(MANAGEMENT_THREAD_WAITING_COUNT_METRIC_NAME, ElasticsearchDatatype.INTEGER));

    @Override
    public void initialize(EsacMetricRegistry metricRegistry) {
        metrics.put(MANAGEMENT_THREAD_BLOCKED_COUNT_METRIC_NAME,
            ((Gauge<Integer>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getThreadSnapshot().getStateThreadCounts().get(State.BLOCKED)));
        metrics.put(MANAGEMENT_THREAD_COUNT_METRIC_NAME,
            ((Gauge<Integer>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getThreadSnapshot().getThreadCount()));
        metrics.put(MANAGEMENT_THREAD_DAEMON_COUNT_METRIC_NAME,
            ((Gauge<Integer>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getThreadSnapshot().getDaemonThreadCount()));
        metrics.put(MANAGEMENT_THREAD_DEADLOCKED_COUNT_METRIC_NAME,
            ((Gauge<Integer>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getThreadSnapshot().getDeadlockedThreadCount()));
        metrics.put(MANAGEMENT_THREAD_DEADLOCKED_MONITOR_COUNT_METRIC_NAME,
            ((Gauge<Integer>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getThreadSnapshot().getMonitorDeadlockedThreadCount()));
        metrics.put(MANAGEMENT_THREAD_DEADLOCKED_SYNCHRONIZER_COUNT_METRIC_NAME,
            ((Gauge<Integer>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getThreadSnapshot().getSynchronizerDeadlockedThreadCount()));
        metrics.put(MANAGEMENT_THREAD_NATIVE_COUNT_METRIC_NAME,
            ((Gauge<Integer>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getThreadSnapshot().getNativeThreadCount()));
        metrics.put(MANAGEMENT_THREAD_NEW_COUNT_METRIC_NAME,
            ((Gauge<Integer>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getThreadSnapshot().getStateThreadCounts().get(State.NEW)));
        metrics.put(MANAGEMENT_THREAD_RUNNABLE_COUNT_METRIC_NAME,
            ((Gauge<Integer>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getThreadSnapshot().getStateThreadCounts().get(State.RUNNABLE)));
        metrics.put(MANAGEMENT_THREAD_TERMINATED_COUNT_METRIC_NAME,
            ((Gauge<Integer>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getThreadSnapshot().getStateThreadCounts().get(State.TERMINATED)));
        metrics.put(MANAGEMENT_THREAD_TIMED_WAITING_COUNT_METRIC_NAME, ((Gauge<Integer>) () -> metricRegistry.getDataSource(ManagementSnapshot.class)
            .getThreadSnapshot().getStateThreadCounts().get(State.TIMED_WAITING)));
        metrics.put(MANAGEMENT_THREAD_WAITING_COUNT_METRIC_NAME,
            ((Gauge<Integer>) () -> metricRegistry.getDataSource(ManagementSnapshot.class).getThreadSnapshot().getStateThreadCounts().get(State.WAITING)));
    }

    @Nullable
    @Override
    public ElasticsearchFieldMapping[] getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
