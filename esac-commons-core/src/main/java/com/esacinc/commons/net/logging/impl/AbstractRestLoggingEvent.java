package com.esacinc.commons.net.logging.impl;

import com.esacinc.commons.logging.impl.AbstractEsacLoggingEvent;
import com.esacinc.commons.net.logging.RestEndpointType;
import com.esacinc.commons.net.logging.RestEventType;
import com.esacinc.commons.net.logging.RestLoggingEvent;

public abstract class AbstractRestLoggingEvent extends AbstractEsacLoggingEvent implements RestLoggingEvent {
    protected RestEventType eventType;
    protected RestEndpointType endpointType;
    protected String txId;

    protected AbstractRestLoggingEvent(RestEventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public RestEndpointType getEndpointType() {
        return this.endpointType;
    }

    @Override
    public void setEndpointType(RestEndpointType endpointType) {
        this.endpointType = endpointType;
    }

    @Override
    public RestEventType getEventType() {
        return this.eventType;
    }

    @Override
    public String getTxId() {
        return this.txId;
    }

    @Override
    public void setTxId(String txId) {
        this.txId = txId;
    }
}
