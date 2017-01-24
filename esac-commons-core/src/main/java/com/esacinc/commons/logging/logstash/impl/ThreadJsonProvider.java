package com.esacinc.commons.logging.logstash.impl;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldNames;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;

public class ThreadJsonProvider extends AbstractEsacJsonProvider {
    private final static String THREAD_NAME_FIELD_NAME = "thread." + ElasticsearchFieldNames.NAME;

    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(THREAD_NAME_FIELD_NAME, ElasticsearchDatatype.TEXT));

    @Override
    protected void writeToInternal(PropertyTrie<Object> fields, LoggingEvent event) {
        fields.put(THREAD_NAME_FIELD_NAME, event.getThreadName());
    }

    @Nullable
    @Override
    public ElasticsearchFieldMapping[] getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
