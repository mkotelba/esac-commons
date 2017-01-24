package com.esacinc.commons.net.logging;

import com.esacinc.commons.beans.NamedBean;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RestEndpointType implements NamedBean {
    SERVER, CLIENT;

    private final String name;

    private RestEndpointType() {
        this.name = this.name().toLowerCase();
    }

    @JsonValue
    @Override
    public String getName() {
        return this.name;
    }
}
