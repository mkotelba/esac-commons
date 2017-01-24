package com.esacinc.commons.ws.jaxws.client.impl;

import com.esacinc.commons.ws.client.impl.AbstractEsacWsClientResponse;
import com.esacinc.commons.ws.jaxws.client.JaxWsClientResponse;
import org.apache.cxf.endpoint.Client;

public class JaxWsClientResponseImpl extends AbstractEsacWsClientResponse<Client, Client, Object[]> implements JaxWsClientResponse {
    public JaxWsClientResponseImpl(Client delegate, Client invocationDelegate, Object ... result) {
        super(delegate, invocationDelegate, result);
    }
}
