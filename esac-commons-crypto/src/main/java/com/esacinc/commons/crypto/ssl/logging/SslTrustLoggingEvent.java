package com.esacinc.commons.crypto.ssl.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchDatatype;
import com.esacinc.commons.logging.elasticsearch.ElasticsearchField;
import java.security.cert.X509Certificate;
import javax.annotation.Nullable;

public interface SslTrustLoggingEvent extends SslLoggingEvent {
    @JsonProperty
    @Nullable
    public String getAuthType();

    public void setAuthType(@Nullable String authType);

    public boolean hasCertificates();

    @ElasticsearchField(datatype = ElasticsearchDatatype.NESTED)
    @JsonProperty("certificate")
    @Nullable
    public X509Certificate[] getCertificates();

    public void setCertificates(@Nullable X509Certificate[] certs);

    public boolean hasPathCertificates();

    @ElasticsearchField(datatype = ElasticsearchDatatype.NESTED)
    @JsonProperty("path_certificate")
    @Nullable
    public X509Certificate[] getPathCertificates();

    public void setPathCertificates(@Nullable X509Certificate[] pathCerts);

    public boolean hasTrustAnchorCertificate();

    @JsonProperty("trust_anchor_certificate")
    @Nullable
    public X509Certificate getTrustAnchorCertificate();

    public void setTrustAnchorCertificate(@Nullable X509Certificate trustAnchorCert);

    @JsonProperty
    public boolean isTrusted();

    public void setTrusted(boolean trusted);
}
