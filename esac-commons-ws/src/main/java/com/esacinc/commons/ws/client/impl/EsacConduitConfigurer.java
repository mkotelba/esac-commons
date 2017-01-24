package com.esacinc.commons.ws.client.impl;

import org.apache.cxf.Bus;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transport.http.HTTPConduitConfigurer;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.beans.factory.InitializingBean;

public class EsacConduitConfigurer implements HTTPConduitConfigurer, InitializingBean {
    private Bus bus;
    private HTTPClientPolicy clientPolicy;
    private TLSClientParameters tlsClientParams;

    @Override
    public void configure(String name, String addr, HTTPConduit conduit) {
        conduit.setClient(this.clientPolicy);
        conduit.setTlsClientParameters(this.tlsClientParams);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.bus.setExtension(this, HTTPConduitConfigurer.class);
    }

    public Bus getBus() {
        return this.bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public HTTPClientPolicy getClientPolicy() {
        return this.clientPolicy;
    }

    public void setClientPolicy(HTTPClientPolicy clientPolicy) {
        this.clientPolicy = clientPolicy;
    }

    public TLSClientParameters getTlsClientParameters() {
        return this.tlsClientParams;
    }

    public void setTlsClientParameters(TLSClientParameters tlsClientParams) {
        this.tlsClientParams = tlsClientParams;
    }
}
