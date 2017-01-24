package com.esacinc.commons.crypto.ssl.logging.impl;

import com.esacinc.commons.crypto.ssl.logging.SslLoggingEvent;
import com.esacinc.commons.logging.impl.AbstractEsacLoggingEvent;
import com.esacinc.commons.net.logging.RestEndpointType;

public abstract class AbstractSslLoggingEvent extends AbstractEsacLoggingEvent implements SslLoggingEvent {
    protected RestEndpointType endpointType;

    @Override
    public RestEndpointType getEndpointType() {
        return this.endpointType;
    }

    @Override
    public void setEndpointType(RestEndpointType endpointType) {
        this.endpointType = endpointType;
    }
}
