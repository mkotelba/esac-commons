package com.esacinc.commons.ws.jaxws.client;

import com.esacinc.commons.ws.client.EsacWsClient;
import org.apache.cxf.endpoint.Client;

public interface JaxWsClient extends EsacWsClient<Client, Client, JaxWsClientRequest, Object[], JaxWsClientResponse> {
}
