package com.esacinc.commons.context.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import javax.annotation.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public abstract class AbstractEsacApplicationRunListener<T extends AbstractEsacApplication> implements SpringApplicationRunListener {
    protected T app;
    protected String[] args;

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected AbstractEsacApplicationRunListener(SpringApplication app, String[] args) {
        this.app = ((T) app);
        this.args = args;
    }

    @Override
    public void finished(ConfigurableApplicationContext appContext, @Nullable Throwable exception) {
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext appContext) {
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext appContext) {
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment env) {
    }

    @Override
    public void started() {
    }
}
