package com.esacinc.commons.ws.client;

public interface EsacWsClient<T, U, V extends EsacWsClientRequest<T, U>, W, X extends EsacWsClientResponse<T, U, W>> {
    public X invoke(V req) throws Exception;

    public T getDelegate();
}
