package com.esacinc.commons.management;

import com.esacinc.commons.beans.NamedBean;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface PoolMxBeanSnapshot extends MxBeanSnapshot, NamedBean {
    @JsonProperty
    @Override
    public String getName();
}
