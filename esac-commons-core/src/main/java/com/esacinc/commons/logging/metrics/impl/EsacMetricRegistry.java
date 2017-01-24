package com.esacinc.commons.logging.metrics.impl;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.esacinc.commons.concurrent.impl.ThreadPoolTaskExecutorService;
import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.config.property.impl.PropertyNameComparator;
import com.esacinc.commons.config.property.impl.PropertyTrieImpl;
import com.esacinc.commons.logging.metrics.EsacMetricSet;
import com.esacinc.commons.logging.metrics.MetricDataSourceProvider;
import com.github.rollingmetrics.histogram.HdrBuilder;
import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class EsacMetricRegistry extends MetricRegistry implements InitializingBean {
    private class MetricDataSourceRefreshTask implements Runnable {
        private Class<?> dataSrcClass;
        private MetricDataSourceProvider<?> dataSrcProv;

        public MetricDataSourceRefreshTask(Class<?> dataSrcClass, MetricDataSourceProvider<?> dataSrcProv) {
            this.dataSrcClass = dataSrcClass;
            this.dataSrcProv = dataSrcProv;
        }

        @Override
        public void run() {
            try {
                EsacMetricRegistry.this.dataSrcs.put(this.dataSrcClass, this.dataSrcProv.buildDataSource(true));
            } finally {
                EsacMetricRegistry.this.dataSrcRefreshLatch.countDown();
            }
        }
    }

    private class GaugeSnapshotTask implements Runnable {
        private String gaugeName;
        private Gauge<?> gauge;

        public GaugeSnapshotTask(String gaugeName, Gauge<?> gauge) {
            this.gaugeName = gaugeName;
            this.gauge = gauge;
        }

        @Override
        public void run() {
            try {
                EsacMetricRegistry.this.snapshot.put(this.gaugeName, this.gauge.getValue());
            } finally {
                EsacMetricRegistry.this.snapshotLatch.countDown();
            }
        }
    }

    @Autowired
    private List<MetricDataSourceProvider<?>> dataSrcProvs;

    @Autowired
    private List<EsacMetricSet> metricSets;

    private ConcurrentNavigableMap<String, Metric> metrics;
    private ThreadPoolTaskExecutorService dataSrcTaskExecService;
    private ThreadPoolTaskExecutorService snapshotTaskExecService;
    private int numDataSrcs;
    private ConcurrentMap<Class<?>, Object> dataSrcs;
    private MetricDataSourceRefreshTask[] dataSrcRefreshTasks;
    private CountDownLatch dataSrcRefreshLatch;
    private int numMetrics;
    private ConcurrentNavigableMap<String, Object> snapshot;
    private GaugeSnapshotTask[] snapshotTasks;
    private CountDownLatch snapshotLatch;
    private final Lock snapshotLock = new ReentrantLock();

    public PropertyTrie<Object> buildSnapshot() throws InterruptedException {
        this.snapshotLock.lock();

        try {
            this.dataSrcRefreshLatch = new CountDownLatch(this.numDataSrcs);

            for (MetricDataSourceRefreshTask dataSrcRefreshTask : this.dataSrcRefreshTasks) {
                this.dataSrcTaskExecService.execute(dataSrcRefreshTask);
            }

            this.dataSrcRefreshLatch.await();

            this.snapshot.clear();

            this.snapshotLatch = new CountDownLatch(this.numMetrics);

            for (GaugeSnapshotTask snapshotTask : this.snapshotTasks) {
                this.snapshotTaskExecService.execute(snapshotTask);
            }

            this.snapshotLatch.await();

            PropertyTrie<Object> snapshotTrie = new PropertyTrieImpl<>();
            snapshotTrie.putAll(this.snapshot);

            return snapshotTrie;
        } finally {
            this.snapshotLock.unlock();
        }
    }

    @Override
    public Counter counter(String name) {
        return this.buildMetric(name, Counter.class, SamplingCounter::new);
    }

    @Override
    public Histogram histogram(String name) {
        return this.buildMetric(name, Histogram.class, () -> new HdrBuilder().resetReservoirOnSnapshot().buildHistogram());
    }

    @Override
    public Meter meter(String name) {
        return this.buildMetric(name, Meter.class, SamplingMeter::new);
    }

    @Override
    public Timer timer(String name) {
        return this.buildMetric(name, Timer.class, () -> new HdrBuilder().resetReservoirOnSnapshot().buildTimer());
    }

    public <T> T getDataSource(Class<T> dataSrcClass) {
        return dataSrcClass.cast(this.dataSrcs.get(dataSrcClass));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.dataSrcTaskExecService.setCorePoolSize((this.numDataSrcs = this.dataSrcProvs.size()));

        this.dataSrcTaskExecService.getThreadPoolExecutor().prestartAllCoreThreads();

        this.dataSrcs = new ConcurrentHashMap<>(this.numDataSrcs);

        this.dataSrcRefreshTasks = new MetricDataSourceRefreshTask[this.numDataSrcs];

        MetricDataSourceProvider<?> dataSrcProv;

        for (int a = 0; a < this.numDataSrcs; a++) {
            this.dataSrcRefreshTasks[a] = new MetricDataSourceRefreshTask((dataSrcProv = this.dataSrcProvs.get(a)).getDataSourceClass(), dataSrcProv);
        }

        for (EsacMetricSet metricSet : this.metricSets) {
            metricSet.initialize(this);

            this.registerAll(metricSet);
        }

        this.snapshotTaskExecService.setCorePoolSize((this.numMetrics = this.metrics.size()));

        this.snapshotTaskExecService.getThreadPoolExecutor().prestartAllCoreThreads();

        this.snapshot = new ConcurrentSkipListMap<>(PropertyNameComparator.INSTANCE);

        this.snapshotTasks = this.metrics.entrySet().stream().filter(metricEntry -> (metricEntry.getValue() instanceof Gauge<?>))
            .map(metricEntry -> new GaugeSnapshotTask(metricEntry.getKey(), ((Gauge<?>) metricEntry.getValue()))).toArray(GaugeSnapshotTask[]::new);
    }

    @Override
    protected ConcurrentNavigableMap<String, Metric> buildMap() {
        return (this.metrics = new ConcurrentSkipListMap<>(PropertyNameComparator.INSTANCE));
    }

    private <T extends Metric> T buildMetric(String name, Class<T> metricClass, Supplier<T> metricBuilder) {
        Metric metric = this.metrics.get(name);

        if (metricClass.isInstance(metric)) {
            return metricClass.cast(metric);
        } else if (metric == null) {
            try {
                this.register(name, metricBuilder.get());
            } catch (IllegalArgumentException ignored) {
                if (metricClass.isInstance((metric = this.metrics.get(name)))) {
                    return metricClass.cast(metric);
                }
            }
        }

        throw new IllegalArgumentException(String.format("Metric (name=%s) is already registered as a different type.", name));
    }

    public ThreadPoolTaskExecutor getDataSourceTaskExecutorService() {
        return this.dataSrcTaskExecService;
    }

    public void setDataSourceTaskExecutorService(ThreadPoolTaskExecutorService dataSrcTaskExecService) {
        this.dataSrcTaskExecService = dataSrcTaskExecService;
    }

    @Override
    public NavigableMap<String, Metric> getMetrics() {
        return this.metrics;
    }

    public ThreadPoolTaskExecutorService getSnapshotTaskExecutorService() {
        return this.snapshotTaskExecService;
    }

    public void setSnapshotTaskExecutorService(ThreadPoolTaskExecutorService snapshotTaskExecService) {
        this.snapshotTaskExecService = snapshotTaskExecService;
    }
}
