package com.esacinc.commons.ws.logging.impl;

import com.esacinc.commons.net.logging.RestEventType;
import com.esacinc.commons.ws.logging.WsResponseLoggingEvent;

public class WsResponseLoggingEventImpl extends AbstractWsLoggingEvent implements WsResponseLoggingEvent {
    public WsResponseLoggingEventImpl() {
        super(RestEventType.RESPONSE);
    }
}
