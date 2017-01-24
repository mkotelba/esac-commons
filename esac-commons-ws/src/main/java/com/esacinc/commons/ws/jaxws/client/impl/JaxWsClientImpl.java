package com.esacinc.commons.ws.jaxws.client.impl;

import com.esacinc.commons.ws.client.impl.AbstractEsacWsClient;
import com.esacinc.commons.ws.client.impl.EsacConduitSelector;
import com.esacinc.commons.ws.jaxws.client.JaxWsClient;
import com.esacinc.commons.ws.jaxws.client.JaxWsClientRequest;
import com.esacinc.commons.ws.jaxws.client.JaxWsClientResponse;
import java.util.concurrent.ExecutionException;
import org.apache.cxf.endpoint.Client;

public class JaxWsClientImpl extends AbstractEsacWsClient<Client, Client, JaxWsClientRequest, Object[], JaxWsClientResponse> implements JaxWsClient {
    public JaxWsClientImpl(Client delegate) {
        super(delegate);
    }

    @Override
    public JaxWsClientResponse invoke(JaxWsClientRequest req) throws Exception {
        Client delegate = req.getDelegate(), invocationDelegate = req.getInvocationDelegate();

        try {
            return new JaxWsClientResponseImpl(this.delegate, invocationDelegate, ((EsacConduitSelector) invocationDelegate.getConduitSelector())
                .getTaskExecutor().submit(() -> invocationDelegate.invoke(req.getOperationQname(), req.getParameters())).get());
        } catch (ExecutionException e) {
            throw ((Exception) e.getCause());
        }
    }
}
