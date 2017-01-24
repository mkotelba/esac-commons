package com.esacinc.commons.logging.logback.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.EncoderBase;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;
import com.github.sebhoss.warnings.CompilerWarnings;
import com.esacinc.commons.logging.logback.impl.CachingAppender;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class EsacLogbackUtils {
    private EsacLogbackUtils() {
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static Logger buildLogger(LoggerContext context, String name, Level level, boolean additive,
        Collection<? extends Appender<ILoggingEvent>> appenders) {
        Logger logger = context.getLogger(name);
        logger.setLevel(level);
        logger.setAdditive(additive);

        appenders.forEach(logger::addAppender);

        return logger;
    }

    public static CachingAppender buildCachingAppender(LoggerContext context, String name) {
        return buildAppender(context, new CachingAppender(name), name, true);
    }

    public static FileAppender<ILoggingEvent> buildFileAppender(LoggerContext context, String name, EncoderBase<ILoggingEvent> encoder, File file) {
        FileAppender<ILoggingEvent> appender = buildOutputStreamAppender(context, new FileAppender<>(), name, encoder, false);
        appender.setFile(file.getPath());
        appender.setPrudent(true);
        appender.start();

        return appender;
    }

    public static <T extends OutputStreamAppender<ILoggingEvent>> T buildOutputStreamAppender(LoggerContext context, T appender, String name,
        EncoderBase<ILoggingEvent> encoder, boolean start) {
        buildAppender(context, appender, name, false).setEncoder(encoder);

        if (start) {
            appender.start();
        }

        return appender;
    }

    public static <T extends Appender<ILoggingEvent>> T buildAppender(LoggerContext context, T appender, String name, boolean start) {
        appender.setName(name);

        return buildLifeCycle(context, appender, start);
    }

    public static PatternLayoutEncoder buildPatternLayoutEncoder(LoggerContext context, String pattern) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setCharset(StandardCharsets.UTF_8);
        encoder.setPattern(pattern);

        return buildLifeCycle(context, encoder, true);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static void buildConversionRule(LoggerContext context, String word, Class<? extends Converter<ILoggingEvent>> clazz) {
        Map<String, String> reg = ((Map<String, String>) context.getObject(CoreConstants.PATTERN_RULE_REGISTRY));

        if (reg == null) {
            context.putObject(CoreConstants.PATTERN_RULE_REGISTRY, (reg = new HashMap<>()));
        }

        reg.put(word, clazz.getName());
    }

    public static <T extends LifeCycle> T buildLifeCycle(LoggerContext context, T lifeCycle, boolean start) {
        if (lifeCycle instanceof ContextAware) {
            ((ContextAware) lifeCycle).setContext(context);
        }

        if (start) {
            lifeCycle.start();
        }

        return lifeCycle;
    }
}
