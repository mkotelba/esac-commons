package com.esacinc.commons.ws.jaxrs.client.impl;

import com.esacinc.commons.ws.client.impl.AbstractEsacWsClient;
import com.esacinc.commons.ws.client.impl.EsacConduitSelector;
import com.esacinc.commons.ws.jaxrs.client.JaxRsClientRequest;
import com.esacinc.commons.ws.jaxrs.client.JaxRsClientResponse;
import java.util.concurrent.ExecutionException;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.WebClient;

public class JaxRsClientImpl extends AbstractEsacWsClient<Client, WebClient, JaxRsClientRequest, Response, JaxRsClientResponse> {
    public JaxRsClientImpl(Client delegate) {
        super(delegate);
    }

    @Override
    public JaxRsClientResponse invoke(JaxRsClientRequest req) throws Exception {
        Client delegate = req.getDelegate();
        WebClient invocationDelegate = req.getInvocationDelegate();

        try {
            return new JaxRsClientResponseImpl(this.delegate, invocationDelegate,
                ((EsacConduitSelector) WebClient.getConfig(invocationDelegate).getConduitSelector()).getTaskExecutor()
                    .submit(() -> invocationDelegate.invoke(req.getMethod().name(), req.getEntity())).get());
        } catch (ExecutionException e) {
            throw ((Exception) e.getCause());
        }
    }
}
