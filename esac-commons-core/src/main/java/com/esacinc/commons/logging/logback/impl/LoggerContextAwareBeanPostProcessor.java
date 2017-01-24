package com.esacinc.commons.logging.logback.impl;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextAware;
import ch.qos.logback.core.spi.ContextAware;
import com.esacinc.commons.beans.factory.impl.AbstractEsacBeanPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggerContextAwareBeanPostProcessor extends AbstractEsacBeanPostProcessor<ContextAware> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private LoggerContext loggerContext;

    public LoggerContextAwareBeanPostProcessor() {
        super(ContextAware.class);
    }

    @Override
    protected ContextAware postProcessBeforeInitializationInternal(ContextAware bean, String beanName) throws Exception {
        bean.setContext(this.loggerContext);

        if (bean instanceof LoggerContextAware) {
            ((LoggerContextAware) bean).setLoggerContext(this.loggerContext);
        }

        return bean;
    }
}
