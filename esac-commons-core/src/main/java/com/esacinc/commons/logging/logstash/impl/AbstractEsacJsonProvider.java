package com.esacinc.commons.logging.logstash.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.fasterxml.jackson.core.JsonGenerator;
import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.config.property.json.impl.PropertyTrieJsonGenerator;
import com.esacinc.commons.logging.logstash.EsacJsonProvider;
import java.io.IOException;
import net.logstash.logback.composite.AbstractJsonProvider;

public abstract class AbstractEsacJsonProvider extends AbstractJsonProvider<ILoggingEvent> implements EsacJsonProvider {
    @Override
    public void writeTo(JsonGenerator jsonGen, ILoggingEvent event) throws IOException {
        this.writeToInternal(((PropertyTrieJsonGenerator) jsonGen).getOutputTarget(), ((LoggingEvent) event));
    }

    protected abstract void writeToInternal(PropertyTrie<Object> fields, LoggingEvent event) throws IOException;
}
