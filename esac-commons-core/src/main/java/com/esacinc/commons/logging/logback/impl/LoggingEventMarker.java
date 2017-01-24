package com.esacinc.commons.logging.logback.impl;

import com.esacinc.commons.logging.EsacLoggingEvent;

public class LoggingEventMarker extends AbstractEsacMarker {
    private final static long serialVersionUID = 0L;

    private EsacLoggingEvent event;

    public LoggingEventMarker(EsacLoggingEvent event) {
        super("EVENT");

        this.event = event;
    }

    public EsacLoggingEvent getEvent() {
        return this.event;
    }

    public void setEvent(EsacLoggingEvent event) {
        this.event = event;
    }
}
