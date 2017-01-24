package com.esacinc.commons.net.logging;

import com.fasterxml.jackson.annotation.JsonValue;
import com.esacinc.commons.beans.NamedBean;

public enum RestEventType implements NamedBean {
    REQUEST, RESPONSE;

    private final String name;

    private RestEventType() {
        this.name = this.name().toLowerCase();
    }

    @JsonValue
    @Override
    public String getName() {
        return this.name;
    }
}
