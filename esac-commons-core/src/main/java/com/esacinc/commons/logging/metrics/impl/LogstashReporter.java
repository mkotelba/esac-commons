package com.esacinc.commons.logging.metrics.impl;

import com.codahale.metrics.Reporter;
import com.esacinc.commons.beans.impl.AbstractLifecycleBean;
import com.esacinc.commons.concurrent.impl.ThreadPoolTaskSchedulerService;
import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.logging.logback.utils.EsacMarkerUtils.MarkerBuilder;
import com.esacinc.commons.logging.logstash.EsacLogstashTags;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnegative;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public class LogstashReporter extends AbstractLifecycleBean implements Reporter {
    private class ReportMetricsTask implements Runnable {
        @Override
        public void run() {
            try {
                PropertyTrie<Object> snapshot = LogstashReporter.this.metricRegistry.buildSnapshot();

                LOGGER.info(new MarkerBuilder(EsacLogstashTags.METRICS).setFields(snapshot).build(), String.format("Reporting %d metric(s).", snapshot.size()));
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                LOGGER.error("Unable to report metric(s).", e);
            }
        }
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(LogstashReporter.class);

    @Autowired
    private EsacMetricRegistry metricRegistry;

    private long period;
    private ThreadPoolTaskSchedulerService taskSchedulerService;
    private boolean running;
    private ScheduledFuture<?> taskFuture;

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    protected void stopInternal() {
        this.taskFuture.cancel(true);

        this.running = false;
    }

    @Override
    protected void startInternal() {
        this.taskFuture = this.taskSchedulerService.scheduleAtFixedRate(new ReportMetricsTask(), 0, this.period, TimeUnit.MILLISECONDS);

        this.running = true;
    }

    @Nonnegative
    public long getPeriod() {
        return this.period;
    }

    public void setPeriod(@Nonnegative long period) {
        this.period = period;
    }

    public ThreadPoolTaskScheduler getTaskSchedulerService() {
        return this.taskSchedulerService;
    }

    public void setTaskSchedulerService(ThreadPoolTaskSchedulerService taskSchedulerService) {
        this.taskSchedulerService = taskSchedulerService;
    }
}
