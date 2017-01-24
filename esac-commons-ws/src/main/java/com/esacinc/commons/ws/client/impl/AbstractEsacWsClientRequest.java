package com.esacinc.commons.ws.client.impl;

import com.esacinc.commons.ws.client.EsacWsClientRequest;

public abstract class AbstractEsacWsClientRequest<T, U> extends AbstractEsacWsClientComponent<T, U> implements EsacWsClientRequest<T, U> {
    protected AbstractEsacWsClientRequest(T delegate, U invocationDelegate) {
        super(delegate, invocationDelegate);
    }
}
