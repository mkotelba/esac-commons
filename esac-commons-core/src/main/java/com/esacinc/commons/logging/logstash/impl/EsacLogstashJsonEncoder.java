package com.esacinc.commons.logging.logstash.impl;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import com.esacinc.commons.config.property.PropertyTrie;
import com.esacinc.commons.config.property.impl.PropertyTrieImpl;
import com.esacinc.commons.config.property.json.impl.PropertyTrieJsonGenerator;
import com.esacinc.commons.context.impl.AbstractEsacApplication;
import com.esacinc.commons.io.EsacFileNameExtensions;
import com.esacinc.commons.logging.logback.EsacAppenderNames;
import com.esacinc.commons.logging.logback.impl.CachingAppender;
import com.esacinc.commons.logging.logback.utils.EsacLogbackUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sebhoss.warnings.CompilerWarnings;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import net.logstash.logback.composite.CompositeJsonFormatter;
import net.logstash.logback.composite.JsonProvider;
import net.logstash.logback.composite.JsonProviders;
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class EsacLogstashJsonEncoder extends LoggingEventCompositeJsonEncoder implements InitializingBean {
    private class EsacLogstashJsonFormatter extends CompositeJsonFormatter<ILoggingEvent> {
        public EsacLogstashJsonFormatter() {
            super(EsacLogstashJsonEncoder.this);
        }

        @Override
        protected void writeEventToGenerator(JsonGenerator jsonGen, ILoggingEvent event) throws IOException {
            PropertyTrie<Object> fields = new PropertyTrieImpl<>();

            super.writeEventToGenerator(new PropertyTrieJsonGenerator(jsonGen, fields), event);

            jsonGen.writeObject(fields);
            jsonGen.flush();
        }
    }

    @Autowired(required = false)
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private List<CachingAppender> cachingAppenders;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private AbstractEsacApplication app;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.start();

        LoggerContext context = ((LoggerContext) this.context);
        FileAppender<ILoggingEvent> appender = EsacLogbackUtils.buildFileAppender(context, EsacAppenderNames.LOGSTASH_FILE, this,
            new File(this.app.getLogDirectory(), (this.app.getLoggingLogstashFileName() + FilenameUtils.EXTENSION_SEPARATOR + EsacFileNameExtensions.JSON)));

        if (!CollectionUtils.isEmpty(this.cachingAppenders)) {
            // noinspection OptionalGetWithoutIsPresent
            this.cachingAppenders.stream().filter(cachingAppender -> cachingAppender.getName().equals(EsacAppenderNames.LOGSTASH_FILE)).findFirst().get()
                .resetDelegate(appender);
        }
    }

    @Override
    protected EsacLogstashJsonFormatter createFormatter() {
        return new EsacLogstashJsonFormatter();
    }

    public void setObjectMapper(ObjectMapper objMapper) {
        this.getFormatter().getJsonFactory().setCodec(objMapper);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void setProviderItems(JsonProvider<ILoggingEvent> ... provItems) {
        JsonProviders<ILoggingEvent> provs = this.getProviders();

        Stream.of(provItems).forEach(provs::addProvider);
    }
}
