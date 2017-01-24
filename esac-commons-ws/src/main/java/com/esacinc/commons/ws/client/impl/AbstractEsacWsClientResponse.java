package com.esacinc.commons.ws.client.impl;

import com.esacinc.commons.ws.client.EsacWsClientResponse;

public abstract class AbstractEsacWsClientResponse<T, U, V> extends AbstractEsacWsClientComponent<T, U> implements EsacWsClientResponse<T, U, V> {
    protected V result;

    protected AbstractEsacWsClientResponse(T delegate, U invocationDelegate, V result) {
        super(delegate, invocationDelegate);

        this.result = result;
    }

    @Override
    public V getResult() {
        return this.result;
    }
}
