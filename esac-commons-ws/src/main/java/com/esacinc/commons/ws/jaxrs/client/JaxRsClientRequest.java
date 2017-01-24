package com.esacinc.commons.ws.jaxrs.client;

import com.esacinc.commons.ws.client.EsacWsClientRequest;
import javax.annotation.Nullable;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.http.HttpMethod;

public interface JaxRsClientRequest extends EsacWsClientRequest<Client, WebClient> {
    public boolean hasEntity();

    @Nullable
    public Object getEntity();

    public HttpMethod getMethod();
}
