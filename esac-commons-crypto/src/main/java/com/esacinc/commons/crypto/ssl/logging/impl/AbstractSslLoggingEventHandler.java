package com.esacinc.commons.crypto.ssl.logging.impl;

import com.esacinc.commons.crypto.ssl.logging.SslLoggingEvent;
import com.esacinc.commons.crypto.ssl.logging.SslLoggingEventHandler;
import com.esacinc.commons.logging.impl.AbstractEsacLoggingEventHandler;

public abstract class AbstractSslLoggingEventHandler<T extends SslLoggingEvent> extends AbstractEsacLoggingEventHandler<T> implements SslLoggingEventHandler<T> {
    protected final static String SSL_BEAN_NAME_PREFIX = BEAN_NAME_PREFIX + "Ssl";

    protected final static String SSL_MSG_FORMAT_PREFIX = "SSL %s";
    protected final static String SSL_FULL_MSG_FORMAT_SUFFIX = ").";

    protected AbstractSslLoggingEventHandler(Class<T> eventClass) {
        super(eventClass);
    }
}
