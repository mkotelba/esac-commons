package com.esacinc.commons.ws.logging.impl;

import com.esacinc.commons.net.logging.RestEventType;
import com.esacinc.commons.ws.logging.WsRequestLoggingEvent;

public class WsRequestLoggingEventImpl extends AbstractWsLoggingEvent implements WsRequestLoggingEvent {
    public WsRequestLoggingEventImpl() {
        super(RestEventType.REQUEST);
    }
}
