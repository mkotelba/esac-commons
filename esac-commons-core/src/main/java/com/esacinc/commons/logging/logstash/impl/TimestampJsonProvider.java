package com.esacinc.commons.logging.logstash.impl;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import javax.annotation.Nullable;
import net.logstash.logback.composite.FormattedTimestampJsonProvider;
import org.apache.commons.lang3.ArrayUtils;

public class TimestampJsonProvider extends AbstractEsacJsonProvider {
    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(FormattedTimestampJsonProvider.FIELD_TIMESTAMP, ElasticsearchDatatype.DATE));

    @Override
    protected void writeToInternal(PropertyTrie<Object> fields, LoggingEvent event) {
        fields.put(FormattedTimestampJsonProvider.FIELD_TIMESTAMP, event.getTimeStamp());
    }

    @Nullable
    @Override
    public ElasticsearchFieldMapping[] getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
