package com.esacinc.commons.ws;

import com.esacinc.commons.beans.NamedBean;

public enum WsDirection implements NamedBean {
    INBOUND, OUTBOUND;

    private final String name;

    private WsDirection() {
        this.name = this.name().toLowerCase();
    }

    @Override
    public String getName() {
        return this.name;
    }
}
