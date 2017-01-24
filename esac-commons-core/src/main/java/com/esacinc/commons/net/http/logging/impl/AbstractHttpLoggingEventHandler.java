package com.esacinc.commons.net.http.logging.impl;

import com.esacinc.commons.logging.impl.AbstractEsacLoggingEventHandler;
import com.esacinc.commons.net.http.logging.HttpLoggingEvent;
import com.esacinc.commons.net.http.logging.HttpLoggingEventHandler;
import com.esacinc.commons.utils.EsacStringUtils;

public abstract class AbstractHttpLoggingEventHandler<T extends HttpLoggingEvent> extends AbstractEsacLoggingEventHandler<T> implements HttpLoggingEventHandler<T> {
    protected final static String HTTP_BEAN_NAME_PREFIX = BEAN_NAME_PREFIX + "Http";

    protected final static String HTTP_MSG_FORMAT_PREFIX = "HTTP %s %s";
    protected final static String HTTP_FULL_MSG_FORMAT_PREFIX = HTTP_MSG_FORMAT_PREFIX + " (";

    protected final static String HTTP_FULL_MSG_FORMAT_SUFFIX = ").";

    protected final static String HTTP_SHORT_MSG_FORMAT = HTTP_MSG_FORMAT_PREFIX + EsacStringUtils.PERIOD;

    protected AbstractHttpLoggingEventHandler(Class<T> eventClass) {
        super(eventClass);
    }

    @Override
    public String buildMessage(T event, boolean full) {
        return (full ? this.buildFullMessage(event) : String.format(HTTP_SHORT_MSG_FORMAT, event.getEndpointType().getName(), event.getEventType().getName()));
    }

    protected abstract String buildFullMessage(T event);
}
