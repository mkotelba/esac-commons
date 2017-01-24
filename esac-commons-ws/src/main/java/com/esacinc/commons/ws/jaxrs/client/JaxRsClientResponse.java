package com.esacinc.commons.ws.jaxrs.client;

import com.esacinc.commons.ws.client.EsacWsClientResponse;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.WebClient;

public interface JaxRsClientResponse extends EsacWsClientResponse<Client, WebClient, Response> {
}
