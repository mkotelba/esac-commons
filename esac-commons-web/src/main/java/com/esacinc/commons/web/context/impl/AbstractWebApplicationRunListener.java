package com.esacinc.commons.web.context.impl;

import com.esacinc.commons.context.impl.AbstractEsacApplication;
import com.esacinc.commons.context.impl.AbstractEsacApplicationRunListener;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.ConfigurableWebApplicationContext;

@Order((Ordered.HIGHEST_PRECEDENCE + 2))
public abstract class AbstractWebApplicationRunListener<T extends AbstractEsacApplication> extends AbstractEsacApplicationRunListener<T> {
    protected AbstractWebApplicationRunListener(SpringApplication app, String[] args) {
        super(app, args);
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext appContext) {
        ConfigurableWebApplicationContext webAppContext = ((ConfigurableWebApplicationContext) appContext);
        webAppContext.setServletConfig(new ServletContextConfig(webAppContext.getServletContext()));
    }
}
