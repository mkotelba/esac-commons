package com.esacinc.commons.web.context.impl;

import java.util.Enumeration;
import javax.annotation.Nullable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class ServletContextConfig implements ServletConfig {
    private ServletContext servletContext;

    public ServletContextConfig(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Nullable
    @Override
    public String getInitParameter(String name) {
        return this.servletContext.getInitParameter(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return this.servletContext.getInitParameterNames();
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Nullable
    @Override
    public String getServletName() {
        return this.servletContext.getServletContextName();
    }
}
