package com.esacinc.commons.crypto.credential;

import com.esacinc.commons.crypto.cert.CertificateConfig;
import com.esacinc.commons.crypto.key.KeyPairConfig;
import com.esacinc.commons.crypto.ssl.revocation.OcspRevokeReasonType;
import com.esacinc.commons.crypto.time.IntervalConfig;
import java.util.Date;
import javax.annotation.Nullable;
import org.bouncycastle.cert.ocsp.CertificateStatus;

public interface CredentialConfig extends CredentialDescriptor<KeyPairConfig, IntervalConfig, CertificateConfig> {
    @Nullable
    public CertificateStatus getCertificateStatus();

    public boolean hasRevocationReason();

    @Nullable
    public OcspRevokeReasonType getRevocationReason();

    public void setRevocationReason(@Nullable OcspRevokeReasonType revocationReason);

    public boolean hasRevocationTime();

    @Nullable
    public Date getRevocationTime();

    public void setRevocationTime(@Nullable Date revocationTime);
}
