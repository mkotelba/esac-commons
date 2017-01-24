package com.esacinc.commons.ws.jaxws.server.impl;

import org.apache.cxf.endpoint.Server;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartFactoryBean;

public class JaxWsServerFactoryBean extends org.apache.cxf.jaxws.JaxWsServerFactoryBean implements DisposableBean, InitializingBean, SmartFactoryBean<Server> {
    private Server server;

    @Override
    public Server getObject() throws Exception {
        return this.server;
    }

    @Override
    public void destroy() throws Exception {
        if (this.server != null) {
            this.server.destroy();

            this.server = null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.server = this.create();
    }

    @Override
    public boolean isEagerInit() {
        return false;
    }

    @Override
    public Class<?> getObjectType() {
        return Server.class;
    }

    @Override
    public boolean isPrototype() {
        return false;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
