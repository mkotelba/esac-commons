package com.esacinc.commons.ws.client;

public interface EsacWsClientResponse<T, U, V> extends EsacWsClientComponent<T, U> {
    public V getResult();
}
