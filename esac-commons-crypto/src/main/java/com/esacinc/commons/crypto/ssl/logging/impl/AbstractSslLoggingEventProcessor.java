package com.esacinc.commons.crypto.ssl.logging.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.esacinc.commons.utils.EsacStringUtils;
import com.esacinc.commons.crypto.ssl.logging.SslLoggingEvent;
import com.esacinc.commons.crypto.ssl.logging.SslLoggingEventProcessor;
import com.esacinc.commons.logging.logstash.EsacLogstashTags;
import com.esacinc.commons.logging.logback.utils.EsacMarkerUtils.MarkerBuilder;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

public abstract class AbstractSslLoggingEventProcessor<T extends SslLoggingEvent> implements SslLoggingEventProcessor<T> {
    protected final static String DEBUG_SYS_PROP_NAME = "javax.net.debug";
    protected final static String SSL_DEBUG_SYS_PROP_VALUE = "ssl";

    protected final static Logger LOGGER = ((Logger) LoggerFactory.getLogger(AbstractSslLoggingEventProcessor.class));

    protected ThreadLocal<T> threadEvent;
    protected Set<String> debugSysPropValues = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

    protected AbstractSslLoggingEventProcessor(Supplier<T> eventCreator, String ... debugSysPropValues) {
        this.threadEvent = ThreadLocal.withInitial(eventCreator);

        this.debugSysPropValues.add(SSL_DEBUG_SYS_PROP_VALUE);
        Stream.of(debugSysPropValues).forEach(this.debugSysPropValues::add);
    }

    @Override
    public boolean canProcessEvent(StackTraceElement[] frames) {
        String debugSysPropValue = System.getProperty(DEBUG_SYS_PROP_NAME);

        return (!StringUtils.isBlank(debugSysPropValue) && this.debugSysPropValues.containsAll(Arrays.asList(EsacStringUtils.splitTokens(debugSysPropValue))));
    }

    protected void dispatchEvent(StackTraceElement[] frames, Level level, T event) {
        LoggingEvent loggingEvent = new LoggingEvent(Logger.FQCN, LOGGER, level, null, null, null);
        loggingEvent.setCallerData(frames);
        loggingEvent.setMarker(new MarkerBuilder(EsacLogstashTags.SSL).setEvent(event).build());

        LOGGER.callAppenders(loggingEvent);

        this.threadEvent.remove();
    }
}
