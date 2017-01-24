package com.esacinc.commons.logging;

import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapper;
import org.springframework.beans.factory.NamedBean;

public interface EsacLoggingEventHandler<T extends EsacLoggingEvent> extends ElasticsearchFieldMapper, NamedBean {
    public String buildMessage(T event, boolean full);

    public String buildFieldName(T event);

    public boolean canHandle(EsacLoggingEvent event);

    public Class<T> getEventClass();
}
