package com.esacinc.commons.ws.client.impl;

import com.esacinc.commons.ws.client.EsacWsClientComponent;

public abstract class AbstractEsacWsClientComponent<T, U> implements EsacWsClientComponent<T, U> {
    protected T delegate;
    protected U invocationDelegate;
    
    protected AbstractEsacWsClientComponent(T delegate, U invocationDelegate) {
        this.delegate = delegate;
        this.invocationDelegate = invocationDelegate;
    }

    @Override
    public T getDelegate() {
        return this.delegate;
    }

    @Override
    public U getInvocationDelegate() {
        return this.invocationDelegate;
    }
}
