package com.esacinc.commons.logging.logstash;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchFieldMapper;
import net.logstash.logback.composite.JsonProvider;

public interface EsacJsonProvider extends ElasticsearchFieldMapper, JsonProvider<ILoggingEvent> {
}
