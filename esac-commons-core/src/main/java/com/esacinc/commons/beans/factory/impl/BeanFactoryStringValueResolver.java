package com.esacinc.commons.beans.factory.impl;

import javax.annotation.Nullable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.util.StringValueResolver;

public class BeanFactoryStringValueResolver implements StringValueResolver {
    private ConfigurableBeanFactory beanFactory;

    public BeanFactoryStringValueResolver(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Nullable
    @Override
    public String resolveStringValue(String str) {
        return this.beanFactory.resolveEmbeddedValue(str);
    }
}
