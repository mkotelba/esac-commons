package com.esacinc.commons.ws.jaxrs.client;

import com.esacinc.commons.ws.client.EsacWsClient;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.WebClient;

public interface JaxRsClient extends EsacWsClient<Client, WebClient, JaxRsClientRequest, Response, JaxRsClientResponse> {
}
