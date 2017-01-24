package com.esacinc.commons.ws.jaxws.client.impl;

import com.esacinc.commons.ws.client.impl.AbstractEsacWsClientRequest;
import com.esacinc.commons.ws.jaxws.client.JaxWsClientRequest;
import javax.xml.namespace.QName;
import org.apache.cxf.endpoint.Client;

public class JaxWsClientRequestImpl extends AbstractEsacWsClientRequest<Client, Client> implements JaxWsClientRequest {
    private QName opQname;
    private Object[] params;

    public JaxWsClientRequestImpl(Client delegate, QName opQname, Object ... params) {
        super(delegate, delegate);

        this.opQname = opQname;
        this.params = params;
    }

    @Override
    public QName getOperationQname() {
        return this.opQname;
    }

    @Override
    public Object[] getParameters() {
        return this.params;
    }
}
