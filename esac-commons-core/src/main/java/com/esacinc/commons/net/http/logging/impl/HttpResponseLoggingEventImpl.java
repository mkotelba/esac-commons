package com.esacinc.commons.net.http.logging.impl;

import com.esacinc.commons.net.http.logging.HttpResponseLoggingEvent;
import com.esacinc.commons.net.logging.RestEventType;

public class HttpResponseLoggingEventImpl extends AbstractHttpLoggingEvent implements HttpResponseLoggingEvent {
    private Integer statusCode;
    private String statusMsg;

    public HttpResponseLoggingEventImpl() {
        super(RestEventType.RESPONSE);
    }

    @Override
    public Integer getStatusCode() {
        return this.statusCode;
    }

    @Override
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getStatusMessage() {
        return this.statusMsg;
    }

    @Override
    public void setStatusMessage(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
