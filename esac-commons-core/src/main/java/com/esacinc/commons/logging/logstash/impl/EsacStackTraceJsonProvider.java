package com.esacinc.commons.logging.logstash.impl;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapping;
import com.esacinc.commons.logging.elasticsearch.impl.ElasticsearchFieldMappingImpl;
import com.esacinc.commons.logging.logback.impl.RootCauseThrowableProxyConverter;
import javax.annotation.Nullable;
import net.logstash.logback.composite.loggingevent.StackTraceJsonProvider;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class EsacStackTraceJsonProvider extends AbstractEsacJsonProvider {
    private final static ElasticsearchFieldMapping[] FIELD_MAPPINGS =
        ArrayUtils.toArray(new ElasticsearchFieldMappingImpl(StackTraceJsonProvider.FIELD_STACK_TRACE, ElasticsearchDatatype.TEXT));

    @Autowired
    private RootCauseThrowableProxyConverter throwableProxyConv;

    @Override
    protected void writeToInternal(PropertyTrie<Object> fields, LoggingEvent event) {
        IThrowableProxy throwableProxy = event.getThrowableProxy();

        if (throwableProxy != null) {
            fields.put(StackTraceJsonProvider.FIELD_STACK_TRACE, this.throwableProxyConv.throwableProxyToString(throwableProxy));
        }
    }

    @Nullable
    @Override
    public ElasticsearchFieldMapping[] getFieldMappings() {
        return FIELD_MAPPINGS;
    }
}
