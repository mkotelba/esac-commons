package com.esacinc.commons.logging.logstash.impl;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.logging.LoggingMessages;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import com.esacinc.commons.logging.logback.utils.EsacMarkerUtils;
import javax.annotation.Nullable;
import net.logstash.logback.composite.loggingevent.MessageJsonProvider;
import org.apache.commons.lang3.ArrayUtils;

public class MessageMarkerJsonProvider extends AbstractEsacJsonProvider {
    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(MessageJsonProvider.FIELD_MESSAGE, ElasticsearchDatatype.TEXT));

    @Override
    protected void writeToInternal(PropertyTrie<Object> fields, LoggingEvent event) {
        LoggingMessages msgs = EsacMarkerUtils.getMessages(event);

        fields.put(MessageJsonProvider.FIELD_MESSAGE, ((msgs != null) ? msgs.getMessage(false) : event.getFormattedMessage()));
    }

    @Nullable
    @Override
    public ElasticsearchFieldMapping[] getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
