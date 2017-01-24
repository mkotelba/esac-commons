package com.esacinc.commons.logging.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.jul.LevelChangePropagator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.status.StatusUtil;
import ch.qos.logback.core.util.StatusPrinter;
import com.esacinc.commons.context.EsacPropertyNames;
import com.esacinc.commons.context.impl.AbstractEsacApplication;
import com.esacinc.commons.context.impl.AbstractEsacApplicationRunListener;
import com.esacinc.commons.io.EsacFileNameExtensions;
import com.esacinc.commons.logging.EsacLoggingEventHandler;
import com.esacinc.commons.logging.logback.EsacAppender;
import com.esacinc.commons.logging.logback.EsacAppenderNames;
import com.esacinc.commons.logging.logback.impl.DelegatingAppender;
import com.esacinc.commons.logging.logback.impl.EventAppender;
import com.esacinc.commons.logging.logback.impl.MessageMarkerConverter;
import com.esacinc.commons.logging.logback.impl.PriorityColorCompositeConverter;
import com.esacinc.commons.logging.logback.impl.RootCauseThrowableProxyConverter;
import com.esacinc.commons.logging.logback.impl.ThreadSectionConverter;
import com.esacinc.commons.logging.logback.utils.EsacLogbackUtils;
import com.esacinc.commons.tx.logging.logback.impl.TxMdcConverter;
import com.esacinc.commons.utils.EsacStringUtils;
import com.github.sebhoss.warnings.CompilerWarnings;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.bind.PropertySourceUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;

@Order((Ordered.HIGHEST_PRECEDENCE + 1))
public abstract class AbstractLoggingApplicationRunListener<T extends AbstractEsacApplication> extends AbstractEsacApplicationRunListener<T> {
    private final static String CONSOLE_APPENDER_PATTERN = "%xT%" + TxMdcConverter.WORD + "{true}%" + PriorityColorCompositeConverter.WORD + " - %" +
        MessageMarkerConverter.WORD + "%n%" + RootCauseThrowableProxyConverter.WORD;

    private final static String FILE_APPENDER_PATTERN =
        "%d{yyyy-MM-dd HH:mm:ss z} [%C:%L %t] %" + TxMdcConverter.WORD + "%p - %" + MessageMarkerConverter.WORD + "%n%" + RootCauseThrowableProxyConverter.WORD;

    private final static String APPENDER_BEAN_NAME_PREFIX = "appender";

    private final static String LOGGER_CONTEXT_BEAN_NAME = "loggerContext";

    private List<EsacLoggingEventHandler<?>> eventHandlers;
    private LoggerContext context;
    private Map<String, EsacAppender> appenders = new LinkedHashMap<>();

    protected AbstractLoggingApplicationRunListener(SpringApplication app, String[] args) {
        super(app, args);
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext appContext) {
        final ConfigurableListableBeanFactory beanFactory = appContext.getBeanFactory();

        beanFactory.registerSingleton(LOGGER_CONTEXT_BEAN_NAME, this.context);

        this.eventHandlers.forEach(eventHandler -> beanFactory.registerSingleton(eventHandler.getBeanName(), eventHandler));

        this.appenders
            .forEach((appenderName, appender) -> beanFactory.registerSingleton((APPENDER_BEAN_NAME_PREFIX + StringUtils.capitalize(appenderName)), appender));
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void environmentPrepared(ConfigurableEnvironment env) {
        this.context = ContextSelectorStaticBinder.getSingleton().getContextSelector().getDefaultLoggerContext();

        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        this.context.stop();
        this.context.reset();

        LevelChangePropagator lvlChangePropagator = new LevelChangePropagator();
        lvlChangePropagator.setContext(this.context);
        lvlChangePropagator.setResetJUL(true);
        this.context.addListener(lvlChangePropagator);

        this.context.putObject(AbstractEsacApplication.BEAN_NAME, this.app);

        EsacLogbackUtils.buildConversionRule(this.context, MessageMarkerConverter.WORD, MessageMarkerConverter.class);
        EsacLogbackUtils.buildConversionRule(this.context, PriorityColorCompositeConverter.WORD, PriorityColorCompositeConverter.class);
        EsacLogbackUtils.buildConversionRule(this.context, RootCauseThrowableProxyConverter.WORD, RootCauseThrowableProxyConverter.class);
        EsacLogbackUtils.buildConversionRule(this.context, ThreadSectionConverter.WORD, ThreadSectionConverter.class);
        EsacLogbackUtils.buildConversionRule(this.context, TxMdcConverter.WORD, TxMdcConverter.class);

        this.buildAppender(EsacAppenderNames.EVENT, EsacLogbackUtils.buildAppender(this.context,
            new EventAppender(this.app.getName(), this.app.getInstanceId(), this.app.getPid(), this.eventHandlers), EsacAppenderNames.EVENT, true));

        this.buildAppender(EsacAppenderNames.CONSOLE, EsacLogbackUtils.buildOutputStreamAppender(this.context, new ConsoleAppender<>(),
            EsacAppenderNames.CONSOLE, EsacLogbackUtils.buildPatternLayoutEncoder(this.context, CONSOLE_APPENDER_PATTERN), true));

        this.buildAppender(EsacAppenderNames.FILE,
            EsacLogbackUtils.buildFileAppender(this.context, EsacAppenderNames.FILE,
                EsacLogbackUtils.buildPatternLayoutEncoder(this.context, FILE_APPENDER_PATTERN),
                new File(this.app.getLogDirectory(), (this.app.getLoggingFileName() + FilenameUtils.EXTENSION_SEPARATOR + EsacFileNameExtensions.LOG))));

        this.buildAppender(EsacAppenderNames.LOGSTASH_FILE, EsacLogbackUtils.buildCachingAppender(this.context, EsacAppenderNames.LOGSTASH_FILE));

        EsacLogbackUtils.buildLogger(this.context, org.slf4j.Logger.ROOT_LOGGER_NAME, Level.WARN, true, this.appenders.values());

        PropertySourceUtils.getSubProperties(env.getPropertySources(), (this.app.getPropertyNamePrefix() + EsacPropertyNames.LOGGING_LOGGER_PREFIX))
            .forEach((loggerName, loggerPropValue) -> this.buildLogger(loggerName, ((String) loggerPropValue)));

        StatusManager statusManager = this.context.getStatusManager();
        StatusUtil statusUtil = new StatusUtil(statusManager);
        long lastResetTime = statusUtil.timeOfLastReset();

        if (statusUtil.getHighestLevel(lastResetTime) >= Status.WARN) {
            StatusPrinter.print(statusManager, lastResetTime);

            throw new ApplicationContextException(String.format("Unable to initialize application (name=%s) logging.", this.app.getName()));
        }

        this.context.getLogger(AbstractLoggingApplicationRunListener.class)
            .info(String.format("Application (name=%s) logging initialized.", this.app.getName()));
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.RAWTYPES, CompilerWarnings.UNCHECKED })
    public void started() {
        this.eventHandlers = ((List<EsacLoggingEventHandler<?>>) ((List<? extends EsacLoggingEventHandler>) SpringFactoriesLoader
            .loadFactories(EsacLoggingEventHandler.class, Thread.currentThread().getContextClassLoader())));
    }

    private void buildLogger(String loggerName, String loggerPropValue) {
        String[] loggerPropValueParts = StringUtils.split(loggerPropValue, EsacStringUtils.COLON, 2);
        Level loggerLvl = Level.toLevel(loggerPropValueParts[0].toUpperCase(), null);

        if (loggerLvl == null) {
            throw new ApplicationContextException(
                String.format("Unknown application (name=%s) logger (name=%s) level: %s", this.app.getName(), loggerName, loggerPropValueParts[0]));
        }

        List<EsacAppender> loggerAppenders;

        if (loggerPropValueParts.length == 2) {
            String[] loggerAppenderNames = EsacStringUtils.tokenize(StringUtils.trimToNull(loggerPropValueParts[1]));

            (loggerAppenders = new ArrayList<>((loggerAppenderNames.length + 1))).add(this.appenders.get(EsacAppenderNames.EVENT));

            for (String loggerAppenderName : loggerAppenderNames) {
                if (!this.appenders.containsKey(loggerAppenderName)) {
                    throw new ApplicationContextException(String.format("Unknown application (name=%s) logger (name=%s) appender (name=%s).",
                        this.app.getName(), loggerName, loggerAppenderName));
                } else if (!loggerAppenderName.equals(EsacAppenderNames.EVENT)) {
                    loggerAppenders.add(this.appenders.get(loggerAppenderName));
                }
            }
        } else {
            loggerAppenders = new ArrayList<>(this.appenders.values());
        }

        EsacLogbackUtils.buildLogger(context, loggerName, loggerLvl, false, loggerAppenders);
    }

    private void buildAppender(String name, Appender<ILoggingEvent> appender) {
        this.appenders.put(name, ((appender instanceof EsacAppender) ? ((EsacAppender) appender) : new DelegatingAppender(name, appender)));
    }
}
