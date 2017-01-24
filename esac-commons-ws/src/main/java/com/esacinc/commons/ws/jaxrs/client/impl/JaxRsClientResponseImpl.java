package com.esacinc.commons.ws.jaxrs.client.impl;

import com.esacinc.commons.ws.client.impl.AbstractEsacWsClientResponse;
import com.esacinc.commons.ws.jaxrs.client.JaxRsClientResponse;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.WebClient;

public class JaxRsClientResponseImpl extends AbstractEsacWsClientResponse<Client, WebClient, Response> implements JaxRsClientResponse {
    public JaxRsClientResponseImpl(Client delegate, WebClient invocationDelegate, Response result) {
        super(delegate, invocationDelegate, result);
    }
}
