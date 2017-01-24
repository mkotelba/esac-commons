package com.esacinc.commons.web.tomcat.crypto.impl;

import java.security.KeyManagementException;
import java.security.SecureRandom;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.TrustManager;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;
import org.apache.tomcat.util.net.SSLUtil;
import org.apache.tomcat.util.net.jsse.JSSEImplementation;
import org.springframework.beans.factory.InitializingBean;

public class EsacJsseImplementation extends JSSEImplementation implements InitializingBean {
    public class EsacJsseContext implements org.apache.tomcat.util.net.SSLContext {
        @Override
        public void destroy() {
        }

        @Override
        public SSLEngine createSSLEngine() {
            return EsacJsseImplementation.this.context.createSSLEngine();
        }

        @Override
        public void init(KeyManager[] keyManagers, TrustManager[] trustManagers, SecureRandom secureRandom) throws KeyManagementException {
        }

        @Nullable
        @Override
        public SSLSessionContext getServerSessionContext() {
            return EsacJsseImplementation.this.context.getServerSessionContext();
        }

        @Override
        public SSLServerSocketFactory getServerSocketFactory() {
            return EsacJsseImplementation.this.serverSocketFactory;
        }

        @Override
        public SSLParameters getSupportedSSLParameters() {
            return EsacJsseImplementation.this.params;
        }
    }

    public class EsacJsseUtil implements SSLUtil {
        private EsacJsseContext context = new EsacJsseContext();

        @Override
        public EsacJsseContext createSSLContext(List<String> negotiableProtocols) throws Exception {
            return this.context;
        }

        public void configureServerSessionContext() {
            SSLSessionContext serverSessionContext = this.context.getServerSessionContext();

            if (serverSessionContext != null) {
                this.configureSessionContext(serverSessionContext);
            }
        }

        @Override
        public void configureSessionContext(SSLSessionContext sessionContext) {
            sessionContext.setSessionCacheSize(1);
            sessionContext.setSessionTimeout(1);
        }

        public EsacJsseContext getContext() {
            return this.context;
        }

        @Override
        public String[] getEnabledCiphers() throws IllegalArgumentException {
            return EsacJsseImplementation.this.params.getCipherSuites();
        }

        @Override
        public String[] getEnabledProtocols() throws IllegalArgumentException {
            return EsacJsseImplementation.this.params.getProtocols();
        }

        @Override
        public KeyManager[] getKeyManagers() throws Exception {
            return EsacJsseImplementation.this.keyManagers;
        }

        @Override
        public TrustManager[] getTrustManagers() throws Exception {
            return EsacJsseImplementation.this.trustManagers;
        }
    }

    public final static String NAME = EsacJsseImplementation.class.getName();

    private KeyManager[] keyManagers;
    private SSLParameters params;
    private TrustManager[] trustManagers;
    private SSLContext context;
    private SSLServerSocketFactory serverSocketFactory;
    private EsacJsseUtil util;

    @Override
    public EsacJsseUtil getSSLUtil(SSLHostConfigCertificate hostConfigCert) {
        return this.util;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.serverSocketFactory = this.context.getServerSocketFactory();
        this.util = new EsacJsseUtil();
    }

    public SSLContext getContext() {
        return this.context;
    }

    public void setContext(SSLContext context) {
        this.context = context;
    }

    public KeyManager[] getKeyManagers() {
        return this.keyManagers;
    }

    public void setKeyManagers(KeyManager ... keyManagers) {
        this.keyManagers = keyManagers;
    }

    public SSLParameters getParameters() {
        return this.params;
    }

    public void setParameters(SSLParameters params) {
        this.params = params;
    }

    public TrustManager[] getTrustManagers() {
        return this.trustManagers;
    }

    public void setTrustManagers(TrustManager ... trustManagers) {
        this.trustManagers = trustManagers;
    }
}
