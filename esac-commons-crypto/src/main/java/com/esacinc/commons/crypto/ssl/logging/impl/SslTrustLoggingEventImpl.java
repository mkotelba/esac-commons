package com.esacinc.commons.crypto.ssl.logging.impl;

import com.esacinc.commons.crypto.ssl.logging.SslTrustLoggingEvent;
import java.security.cert.X509Certificate;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;

public class SslTrustLoggingEventImpl extends AbstractSslLoggingEvent implements SslTrustLoggingEvent {
    private String authType;
    private X509Certificate[] certs;
    private X509Certificate[] pathCerts;
    private X509Certificate trustAnchorCert;
    private boolean trusted;

    @Nullable
    @Override
    public String getAuthType() {
        return this.authType;
    }

    @Override
    public void setAuthType(@Nullable String authType) {
        this.authType = authType;
    }

    @Override
    public boolean hasCertificates() {
        return !ArrayUtils.isEmpty(this.certs);
    }

    @Nullable
    @Override
    public X509Certificate[] getCertificates() {
        return this.certs;
    }

    @Override
    public void setCertificates(@Nullable X509Certificate[] certs) {
        this.certs = certs;
    }

    @Override
    public boolean hasPathCertificates() {
        return !ArrayUtils.isEmpty(this.pathCerts);
    }

    @Nullable
    @Override
    public X509Certificate[] getPathCertificates() {
        return this.pathCerts;
    }

    @Override
    public void setPathCertificates(@Nullable X509Certificate[] pathCerts) {
        this.pathCerts = pathCerts;
    }

    @Override
    public boolean hasTrustAnchorCertificate() {
        return (this.trustAnchorCert != null);
    }

    @Nullable
    @Override
    public X509Certificate getTrustAnchorCertificate() {
        return this.trustAnchorCert;
    }

    @Override
    public void setTrustAnchorCertificate(@Nullable X509Certificate trustAnchorCert) {
        this.trustAnchorCert = trustAnchorCert;
    }

    @Override
    public boolean isTrusted() {
        return this.trusted;
    }

    @Override
    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }
}
