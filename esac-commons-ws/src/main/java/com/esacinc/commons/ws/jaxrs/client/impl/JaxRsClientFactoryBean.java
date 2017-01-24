package com.esacinc.commons.ws.jaxrs.client.impl;

import org.apache.cxf.jaxrs.client.spring.JAXRSClientFactoryBeanDefinitionParser.JAXRSSpringClientFactoryBean;
import org.springframework.beans.factory.SmartFactoryBean;

public class JaxRsClientFactoryBean extends JAXRSSpringClientFactoryBean implements SmartFactoryBean<JaxRsClientImpl> {
    public JaxRsClientFactoryBean() {
        super();
    }

    public JaxRsClientFactoryBean(String addr) {
        super();

        this.setAddress(addr);
    }

    @Override
    public JaxRsClientImpl getObject() throws Exception {
        return new JaxRsClientImpl(this.create());
    }

    @Override
    public boolean isEagerInit() {
        return false;
    }

    @Override
    public Class<?> getObjectType() {
        return JaxRsClientImpl.class;
    }

    @Override
    public boolean isPrototype() {
        return true;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
