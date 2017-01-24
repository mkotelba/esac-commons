package com.esacinc.commons.logging.logback.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.LogbackException;
import javax.annotation.Nullable;

public class DelegatingAppender extends AbstractEsacAppender {
    protected Appender<ILoggingEvent> delegate;

    public DelegatingAppender(String name, @Nullable Appender<ILoggingEvent> delegate) {
        super(name);

        this.delegate = delegate;
        this.started = true;
    }

    public void resetDelegate(@Nullable Appender<ILoggingEvent> delegate) {
        this.lock.lock();

        try {
            this.delegate = delegate;

            this.resetDelegateInternal();
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    protected void stopInternal() {
        if (this.delegate != null) {
            this.delegate.stop();
        }
    }

    protected void resetDelegateInternal() {
    }

    @Override
    protected void doAppendInternal(LoggingEvent event) throws LogbackException {
        if (this.delegate != null) {
            this.delegate.doAppend(event);
        }
    }

    @Override
    protected void startInternal() {
        if (this.delegate != null) {
            this.delegate.start();
        }
    }
}
