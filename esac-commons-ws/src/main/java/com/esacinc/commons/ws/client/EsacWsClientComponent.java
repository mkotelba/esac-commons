package com.esacinc.commons.ws.client;

public interface EsacWsClientComponent<T, U> {
    public T getDelegate();

    public U getInvocationDelegate();
}
