package com.esacinc.commons.ws.jaxws.client.impl;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartFactoryBean;

public class JaxWsClientFactoryBean extends JaxWsProxyFactoryBean implements DisposableBean, SmartFactoryBean<JaxWsClientImpl> {
    private ClientProxy clientProxy;

    public JaxWsClientFactoryBean() {
        super();
    }

    public JaxWsClientFactoryBean(String addr) {
        super();

        this.setAddress(addr);
    }

    @Override
    public JaxWsClientImpl getObject() throws Exception {
        return new JaxWsClientImpl(((Client) this.create()));
    }

    @Override
    public Object create() {
        this.configured = true;

        return super.create();
    }

    @Override
    public void destroy() throws Exception {
        if (this.clientProxy != null) {
            this.clientProxy.close();

            this.clientProxy = null;
        }
    }

    @Override
    protected ClientProxy clientClientProxy(Client client) {
        return (this.clientProxy = super.clientClientProxy(client));
    }

    @Override
    public boolean isEagerInit() {
        return false;
    }

    @Override
    public Class<?> getObjectType() {
        return JaxWsClientImpl.class;
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
