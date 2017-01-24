package com.esacinc.commons.ws.client.impl;

import com.esacinc.commons.ws.client.EsacWsClient;
import com.esacinc.commons.ws.client.EsacWsClientRequest;
import com.esacinc.commons.ws.client.EsacWsClientResponse;

public abstract class AbstractEsacWsClient<T, U, V extends EsacWsClientRequest<T, U>, W, X extends EsacWsClientResponse<T, U, W>>
    implements EsacWsClient<T, U, V, W, X> {
    protected T delegate;

    protected AbstractEsacWsClient(T delegate) {
        this.delegate = delegate;
    }

    @Override
    public T getDelegate() {
        return this.delegate;
    }
}
