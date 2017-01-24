package com.esacinc.commons.ws.jaxws.client;

import com.esacinc.commons.ws.client.EsacWsClientRequest;
import javax.xml.namespace.QName;
import org.apache.cxf.endpoint.Client;

public interface JaxWsClientRequest extends EsacWsClientRequest<Client, Client> {
    public QName getOperationQname();
    
    public Object[] getParameters();
}
