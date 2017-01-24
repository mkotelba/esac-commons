package com.esacinc.commons.management;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchField;
import com.esacinc.commons.management.impl.EsacMemoryUsage;
import java.lang.management.MemoryMXBean;
import java.util.List;
import javax.annotation.Nonnegative;

public interface MemoryMxBeanSnapshot extends MxBeanSnapshot, MemoryMXBean {
    @ElasticsearchField(datatype = ElasticsearchDatatype.NESTED)
    @JsonProperty("buffer_pool")
    public List<BufferPoolMxBeanSnapshot> getBufferPoolSnapshots();

    @JsonProperty("heap")
    @Override
    public EsacMemoryUsage getHeapMemoryUsage();

    @JsonProperty("non_heap")
    @Override
    public EsacMemoryUsage getNonHeapMemoryUsage();

    @JsonProperty("object_pending_finalization")
    @Nonnegative
    @Override
    public int getObjectPendingFinalizationCount();

    @ElasticsearchField(datatype = ElasticsearchDatatype.NESTED)
    @JsonProperty("pool")
    public List<MemoryPoolMxBeanSnapshot> getPoolSnapshots();
}
