package com.esacinc.commons.logging.impl;

import com.esacinc.commons.logging.EsacLoggingEvent;
import com.esacinc.commons.logging.EsacLoggingEventHandler;

public abstract class AbstractEsacLoggingEventHandler<T extends EsacLoggingEvent> implements EsacLoggingEventHandler<T> {
    protected final static String BEAN_NAME_PREFIX = "eventHandlerLogging";
    
    protected Class<T> eventClass;

    protected AbstractEsacLoggingEventHandler(Class<T> eventClass) {
        this.eventClass = eventClass;
    }

    @Override
    public boolean canHandle(EsacLoggingEvent event) {
        return this.eventClass.isAssignableFrom(event.getClass());
    }

    @Override
    public Class<T> getEventClass() {
        return this.eventClass;
    }
}
