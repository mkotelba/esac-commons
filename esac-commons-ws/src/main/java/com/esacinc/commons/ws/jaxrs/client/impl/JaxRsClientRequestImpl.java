package com.esacinc.commons.ws.jaxrs.client.impl;

import com.esacinc.commons.ws.client.impl.AbstractEsacWsClientRequest;
import com.esacinc.commons.ws.jaxrs.client.JaxRsClientRequest;
import javax.annotation.Nullable;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.http.HttpMethod;

public class JaxRsClientRequestImpl extends AbstractEsacWsClientRequest<Client, WebClient> implements JaxRsClientRequest {
    private HttpMethod method;
    private Object entity;

    public JaxRsClientRequestImpl(Client delegate, WebClient invocationDelegate, HttpMethod method, @Nullable Object entity) {
        super(delegate, invocationDelegate);

        this.method = method;
        this.entity = entity;
    }

    @Override
    public boolean hasEntity() {
        return (this.entity != null);
    }

    @Nullable
    @Override
    public Object getEntity() {
        return this.entity;
    }

    @Override
    public HttpMethod getMethod() {
        return this.method;
    }
}
