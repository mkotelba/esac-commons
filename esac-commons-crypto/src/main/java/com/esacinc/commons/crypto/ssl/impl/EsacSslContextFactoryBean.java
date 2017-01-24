package com.esacinc.commons.crypto.ssl.impl;

import com.esacinc.commons.utils.EsacMethodUtils;
import com.esacinc.commons.crypto.impl.AbstractCryptoServiceFactoryBean;
import com.esacinc.commons.logging.logstash.EsacLogstashTags;
import com.esacinc.commons.logging.logback.utils.EsacMarkerUtils.MarkerBuilder;
import com.esacinc.commons.time.utils.EsacDateUtils;
import com.esacinc.commons.utils.EsacProxyUtils.EsacProxyBuilder;
import java.security.SecureRandom;
import java.util.Date;
import javax.net.ssl.ExtendedSSLSession;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLContextSpi;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EsacSslContextFactoryBean extends AbstractCryptoServiceFactoryBean<SSLContext> {
    private final static String SSL_CONTEXT_SERVICE_TYPE = SSLContext.class.getSimpleName();

    private final static String ENGINE_CREATE_SSL_ENGINE_METHOD_NAME = "engineCreateSSLEngine";
    private final static String ENGINE_GET_SERVER_SOCKET_FACTORY_METHOD_NAME = "engineGetServerSocketFactory";
    private final static String ENGINE_GET_SOCKET_FACTORY_METHOD_NAME = "engineGetSocketFactory";

    private final static String CREATE_SERVER_SOCKET_METHOD_NAME = "createServerSocket";
    private final static String CREATE_SOCKET_METHOD_NAME = "createSocket";

    private final static String BEGIN_HANDSHAKE_METHOD_NAME = "beginHandshake";

    private final static Logger LOGGER = LoggerFactory.getLogger(EsacSslContextFactoryBean.class);

    private KeyManager[] keyManagers;
    private SSLParameters params;
    private SecureRandom secureRandom;
    private TrustManager[] trustManagers;

    public EsacSslContextFactoryBean() {
        super(SSLContext.class);
    }

    @Override
    public SSLContext getObject() throws Exception {
        return new SSLContext(
            new EsacProxyBuilder<>(SSLContextSpi.class, ((SSLContextSpi) this.prov.getService(SSL_CONTEXT_SERVICE_TYPE, this.algName).newInstance(null)))
                .addMethodAdvice(EsacMethodUtils.matchName(ENGINE_CREATE_SSL_ENGINE_METHOD_NAME),
                    (invocation, method, methodName, args, target) -> this.buildEngine(((SSLEngine) invocation.proceed())))
                .addMethodAdvice(EsacMethodUtils.matchName(ENGINE_GET_SERVER_SOCKET_FACTORY_METHOD_NAME),
                    (invocation, method, methodName, args, target) -> this.buildServerSocketFactory(((SSLServerSocketFactory) invocation.proceed())))
                .addMethodAdvice(EsacMethodUtils.matchName(ENGINE_GET_SOCKET_FACTORY_METHOD_NAME),
                    (invocation, method, methodName, args, target) -> this.buildSocketFactory(((SSLSocketFactory) invocation.proceed())))
                .build(),
            this.prov, this.algName) {
            {
                this.init(EsacSslContextFactoryBean.this.keyManagers, EsacSslContextFactoryBean.this.trustManagers,
                    EsacSslContextFactoryBean.this.secureRandom);
            }
        };
    }

    private SSLSocketFactory buildSocketFactory(SSLSocketFactory socketFactory) {
        return new EsacProxyBuilder<>(SSLSocketFactory.class, socketFactory)
            .addMethodAdvice(EsacMethodUtils.matchName(CREATE_SOCKET_METHOD_NAME), (invocation, method, methodName, args, target) -> {
                SSLSocket socket = ((SSLSocket) invocation.proceed());
                socket.setSSLParameters(this.params);

                return socket;
            }).build();
    }

    private SSLServerSocketFactory buildServerSocketFactory(SSLServerSocketFactory serverSocketFactory) {
        return new EsacProxyBuilder<>(SSLServerSocketFactory.class, serverSocketFactory)
            .addMethodAdvice(EsacMethodUtils.matchName(CREATE_SERVER_SOCKET_METHOD_NAME), (invocation, method, methodName, args, target) -> {
                SSLServerSocket serverSocket = ((SSLServerSocket) invocation.proceed());
                serverSocket.setSSLParameters(this.params);

                return serverSocket;
            }).build();
    }

    private SSLEngine buildEngine(SSLEngine engine) {
        engine.setSSLParameters(this.params);

        return new EsacProxyBuilder<>(SSLEngine.class, engine)
            .addMethodAdvice(EsacMethodUtils.matchName(BEGIN_HANDSHAKE_METHOD_NAME), (invocation, method, methodName, args, target) -> {
                ExtendedSSLSession session = ((ExtendedSSLSession) engine.getSession());

                if (session.isValid()) {
                    session.invalidate();

                    LOGGER.debug(new MarkerBuilder(EsacLogstashTags.SSL).build(),
                        String.format(
                            "Existing SSL session (id=%s, creationTime=%s, lastAccessedTime=%s, peerHost=%s, peerPort=%d, protocol=%s, cipherSuite=%s) invalidated.",
                            Hex.encodeHexString(session.getId()), EsacDateUtils.UTC_TZ_DISPLAY_FORMAT.format(new Date(session.getCreationTime())),
                            EsacDateUtils.UTC_TZ_DISPLAY_FORMAT.format(new Date(session.getLastAccessedTime())), session.getPeerHost(), session.getPeerPort(),
                            session.getProtocol(), session.getCipherSuite()));
                }

                invocation.proceed();

                return null;
            }).build();
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

    public SecureRandom getSecureRandom() {
        return this.secureRandom;
    }

    public void setSecureRandom(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    public TrustManager[] getTrustManagers() {
        return this.trustManagers;
    }

    public void setTrustManagers(TrustManager ... trustManagers) {
        this.trustManagers = trustManagers;
    }
}
