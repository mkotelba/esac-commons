package com.esacinc.commons.logging.logback.impl;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.status.InfoStatus;
import java.util.LinkedList;

public class CachingAppender extends DelegatingAppender {
    private LinkedList<LoggingEvent> eventCache = new LinkedList<>();

    public CachingAppender(String name) {
        super(name, null);
    }

    @Override
    protected void stopInternal() {
        this.eventCache.clear();

        super.stopInternal();
    }

    @Override
    protected void resetDelegateInternal() {
        if (this.delegate == null) {
            return;
        }

        int numCachedEvents = this.eventCache.size();

        while (!this.eventCache.isEmpty()) {
            this.doAppend(this.eventCache.removeFirst());
        }

        this.context.getStatusManager()
            .add(new InfoStatus(String.format("Flushed %d cached logging event(s) to appender (name=%s).", numCachedEvents, this.name), this));
    }

    @Override
    protected void doAppendInternal(LoggingEvent event) throws LogbackException {
        if (this.delegate != null) {
            this.delegate.doAppend(event);
        } else {
            this.eventCache.add(event);
        }
    }
}
