package com.esacinc.commons.management.logging.impl;

import com.esacinc.commons.logging.impl.AbstractEsacLoggingEvent;
import com.esacinc.commons.management.logging.GcLoggingEvent;
import javax.annotation.Nonnegative;

public class GcLoggingEventImpl extends AbstractEsacLoggingEvent implements GcLoggingEvent {
    private String name;
    private String action;
    private String cause;
    private long startTimestamp;
    private long endTimestamp;
    private long duration;

    public GcLoggingEventImpl(String name, String action, String cause, @Nonnegative long startTimestamp, @Nonnegative long endTimestamp,
        @Nonnegative long duration) {
        this.name = name;
        this.action = action;
        this.cause = cause;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.duration = duration;
    }

    @Override
    public String getAction() {
        return this.action;
    }

    @Override
    public String getCause() {
        return this.cause;
    }

    @Nonnegative
    @Override
    public long getDuration() {
        return this.duration;
    }

    @Nonnegative
    @Override
    public long getEndTimestamp() {
        return this.endTimestamp;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Nonnegative
    @Override
    public long getStartTimestamp() {
        return this.startTimestamp;
    }
}
