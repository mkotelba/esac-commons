package com.esacinc.commons.crypto.ssl.impl;

import com.esacinc.commons.crypto.cert.utils.EsacCertificatePathUtils;
import com.esacinc.commons.crypto.ssl.SslManager;
import com.esacinc.commons.crypto.ssl.constraints.impl.EsacConstraintsChecker;
import com.esacinc.commons.crypto.ssl.logging.SslTrustLoggingEvent;
import com.esacinc.commons.crypto.ssl.logging.impl.SslTrustLoggingEventImpl;
import com.esacinc.commons.crypto.ssl.revocation.impl.EsacRevocationChecker;
import com.esacinc.commons.logging.logstash.EsacLogstashTags;
import com.esacinc.commons.logging.logback.utils.EsacMarkerUtils.MarkerBuilder;
import com.esacinc.commons.net.logging.RestEndpointType;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Provider;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertificateException;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import javax.annotation.Nullable;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509ExtendedTrustManager;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class EsacTrustManager extends X509ExtendedTrustManager implements BeanFactoryAware, SslManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(EsacTrustManager.class);

    private Provider prov;
    private String algName;
    private KeyStore keyStore;
    private BeanFactory beanFactory;
    private List<PKIXCertPathChecker> certPathCheckers;
    private String constraintsCheckerBeanName;
    private String id;
    private String name;
    private String revocationCheckerBeanName;
    private PKIXBuilderParameters builderParams;

    public EsacTrustManager(Provider prov, String algName, KeyStore keyStore) {
        this.prov = prov;
        this.algName = algName;
        this.keyStore = keyStore;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        this.checkTrusted(RestEndpointType.CLIENT, certs, authType, null, null, null);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType, @Nullable Socket socket) throws CertificateException {
        this.checkTrusted(RestEndpointType.CLIENT, certs, authType, ((SSLSocket) socket), SSLSocket::isConnected, SSLSocket::getHandshakeSession);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType, @Nullable SSLEngine engine) throws CertificateException {
        this.checkTrusted(RestEndpointType.CLIENT, certs, authType, engine, null, SSLEngine::getHandshakeSession);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        this.checkTrusted(RestEndpointType.SERVER, certs, authType, null, null, null);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType, @Nullable Socket socket) throws CertificateException {
        this.checkTrusted(RestEndpointType.SERVER, certs, authType, ((SSLSocket) socket), SSLSocket::isConnected, SSLSocket::getHandshakeSession);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType, @Nullable SSLEngine engine) throws CertificateException {
        this.checkTrusted(RestEndpointType.SERVER, certs, authType, engine, null, SSLEngine::getHandshakeSession);
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return this.builderParams.getTrustAnchors().stream().map(TrustAnchor::getTrustedCert).filter(Objects::nonNull).toArray(X509Certificate[]::new);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initialize();
    }

    @Override
    public void initialize() throws Exception {
        this.builderParams = new PKIXBuilderParameters(this.keyStore, new X509CertSelector());
        this.builderParams.setRevocationEnabled(false);

        if (this.certPathCheckers != null) {
            this.certPathCheckers.forEach(this.builderParams::addCertPathChecker);
        }
    }

    @Override
    public void reset() {
    }

    private static <T> void checkHandshakeSession(RestEndpointType endpointType, String authType, @Nullable T handshakeSessionContainer,
        @Nullable Function<T, Boolean> handshakeSessionAvailableMapper, @Nullable Function<T, SSLSession> handshakeSessionMapper) throws CertificateException {
        if ((handshakeSessionContainer != null) &&
            ((handshakeSessionAvailableMapper == null) || handshakeSessionAvailableMapper.apply(handshakeSessionContainer))) {
            // noinspection ConstantConditions
            SSLSession handshakeSession = handshakeSessionMapper.apply(handshakeSessionContainer);

            if (handshakeSession == null) {
                throw new CertificateException(
                    String.format("Unable to get SSL %s handshake session from container (class=%s) during certificate chain trust checking (authType=%s).",
                        endpointType.getName(), handshakeSessionContainer.getClass().getName(), authType));
            }
        }
    }

    private <T> void checkTrusted(RestEndpointType endpointType, X509Certificate[] certs, String authType, @Nullable T handshakeSessionContainer,
        @Nullable Function<T, Boolean> handshakeSessionAvailableMapper, @Nullable Function<T, SSLSession> handshakeSessionMapper) throws CertificateException {
        SslTrustLoggingEvent event = new SslTrustLoggingEventImpl();
        event.setEndpointType(endpointType);

        try {
            event.setAuthType(authType);

            boolean certsEmpty = ArrayUtils.isEmpty(certs);

            if (!certsEmpty) {
                event.setCertificates(certs);
            }

            if (StringUtils.isEmpty(authType)) {
                throw new IllegalArgumentException(
                    String.format("SSL %s certificate chain trust checking authentication type must be specified.", endpointType.getName()));
            }

            if (certsEmpty) {
                throw new IllegalArgumentException(
                    String.format("SSL %s trust checking (authType=%s) certificate chain must be specified.", endpointType.name().toLowerCase(), authType));
            }

            checkHandshakeSession(endpointType, authType, handshakeSessionContainer, handshakeSessionAvailableMapper, handshakeSessionMapper);

            try {
                X509CertSelector certSelector = new X509CertSelector();
                certSelector.setCertificate(certs[0]);

                PKIXBuilderParameters certBuilderParams = ((PKIXBuilderParameters) this.builderParams.clone());
                certBuilderParams.setTargetCertConstraints(certSelector);
                certBuilderParams.addCertStore(EsacCertificatePathUtils.buildStore(certs));

                X509Certificate issuerCert = EsacCertificatePathUtils.findRootCertificate(certs[0], certBuilderParams);

                certBuilderParams
                    .addCertPathChecker(((EsacConstraintsChecker) this.beanFactory.getBean(this.constraintsCheckerBeanName, endpointType, issuerCert)));

                certBuilderParams
                    .addCertPathChecker(((EsacRevocationChecker) this.beanFactory.getBean(this.revocationCheckerBeanName, endpointType, issuerCert)));

                CertPathBuilder builder = CertPathBuilder.getInstance(this.algName, this.prov);

                PKIXCertPathBuilderResult builderResult = ((PKIXCertPathBuilderResult) builder.build(certBuilderParams));
                X509Certificate[] pathCerts =
                    builderResult.getCertPath().getCertificates().stream().map(cert -> ((X509Certificate) cert)).toArray(X509Certificate[]::new);
                X509Certificate trustAnchorCert = builderResult.getTrustAnchor().getTrustedCert();

                event.setPathCertificates(pathCerts);
                event.setTrustAnchorCertificate(trustAnchorCert);
                event.setTrusted(true);

                LOGGER.info(new MarkerBuilder(EsacLogstashTags.SSL).setEvent(event).build(), null);
            } catch (Exception e) {
                throw new CertificateException(
                    String.format("Unable to build SSL %s certificate chain for trust checking (authType=%s).", endpointType.getName(), authType), e);
            }
        } catch (Exception e) {
            LOGGER.error(new MarkerBuilder(EsacLogstashTags.SSL).setEvent(event).build(), null, e);

            throw e;
        }
    }

    @Override
    public String getAlgorithmName() {
        return this.algName;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Nullable
    public List<PKIXCertPathChecker> getCertificatePathCheckers() {
        return this.certPathCheckers;
    }

    public void setCertificatePathCheckers(@Nullable List<PKIXCertPathChecker> certPathCheckers) {
        this.certPathCheckers = certPathCheckers;
    }

    public String getConstraintsCheckerBeanName() {
        return this.constraintsCheckerBeanName;
    }

    public void setConstraintsCheckerBeanName(String constraintsCheckerBeanName) {
        this.constraintsCheckerBeanName = constraintsCheckerBeanName;
    }

    @Override
    public boolean hasId() {
        return (this.id != null);
    }

    @Nullable
    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(@Nullable String id) {
        this.id = id;
    }

    @Override
    public KeyStore getKeyStore() {
        return this.keyStore;
    }

    @Override
    public boolean hasName() {
        return (this.name != null);
    }

    @Nullable
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Override
    public Provider getProvider() {
        return this.prov;
    }

    public String getRevocationCheckerBeanName() {
        return this.revocationCheckerBeanName;
    }

    public void setRevocationCheckerBeanName(String revocationCheckerBeanName) {
        this.revocationCheckerBeanName = revocationCheckerBeanName;
    }
}
