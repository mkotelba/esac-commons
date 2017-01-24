package com.esacinc.commons.management;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchField;
import com.esacinc.commons.management.impl.MemoryPoolUsage;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;

public interface MemoryPoolMxBeanSnapshot extends MemoryPoolMXBean, PoolMxBeanSnapshot {
    @ElasticsearchField(datatype = ElasticsearchDatatype.TEXT)
    @JsonProperty
    @Override
    public MemoryType getType();

    @JsonProperty
    @JsonUnwrapped
    @Override
    public MemoryPoolUsage getUsage();
}
