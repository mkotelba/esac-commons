package com.esacinc.commons.logging.logback.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.FilterReply;
import com.esacinc.commons.logging.logback.EsacAppender;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractEsacAppender extends ContextAwareBase implements EsacAppender {
    protected String name;
    protected final Lock lock = new ReentrantLock();
    protected boolean started;

    protected AbstractEsacAppender(String name) {
        this.name = name;
    }

    @Override
    public void stop() {
        this.lock.lock();

        try {
            if (!this.started) {
                return;
            }

            this.stopInternal();

            this.started = false;
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void doAppend(ILoggingEvent event) throws LogbackException {
        this.lock.lock();

        try {
            if (!this.started) {
                return;
            }

            this.doAppendInternal(((LoggingEvent) event));
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void start() {
        this.lock.lock();

        try {
            if (this.started) {
                return;
            }

            this.startInternal();

            this.started = true;
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public FilterReply getFilterChainDecision(ILoggingEvent event) {
        return FilterReply.NEUTRAL;
    }

    @Override
    public void clearAllFilters() {
    }

    @Override
    public void addFilter(Filter<ILoggingEvent> newFilter) {
    }

    @Override
    public List<Filter<ILoggingEvent>> getCopyOfAttachedFiltersList() {
        return new ArrayList<>(0);
    }

    protected void stopInternal() {
    }

    protected void doAppendInternal(LoggingEvent event) throws LogbackException {
    }

    protected void startInternal() {
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isStarted() {
        return this.started;
    }
}
