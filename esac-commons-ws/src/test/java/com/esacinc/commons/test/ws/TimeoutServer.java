package com.esacinc.commons.test.ws;

import com.esacinc.commons.test.beans.EsacHttpServer;
import javax.net.ssl.SSLEngine;

public interface TimeoutServer extends EsacHttpServer {
    public SSLEngine getSslEngine();

    public void setSslEngine(SSLEngine sslEngine);
}
