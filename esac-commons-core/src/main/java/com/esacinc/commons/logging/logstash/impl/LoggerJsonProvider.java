package com.esacinc.commons.logging.logstash.impl;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldNames;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;

public class LoggerJsonProvider extends AbstractEsacJsonProvider {
    private final static String LOGGER_NAME_FIELD_NAME = "logger." + ElasticsearchFieldNames.NAME;

    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(LOGGER_NAME_FIELD_NAME, ElasticsearchDatatype.TEXT));

    @Override
    protected void writeToInternal(PropertyTrie<Object> fields, LoggingEvent event) {
        fields.put(LOGGER_NAME_FIELD_NAME, event.getLoggerName());
    }

    @Nullable
    @Override
    public ElasticsearchFieldMapping[] getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
