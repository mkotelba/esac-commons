package com.esacinc.commons.crypto.credential.impl;

import com.esacinc.commons.crypto.cert.CertificateConfig;
import com.esacinc.commons.crypto.credential.CredentialConfig;
import com.esacinc.commons.crypto.key.KeyPairConfig;
import com.esacinc.commons.crypto.ssl.revocation.OcspRevokeReasonType;
import com.esacinc.commons.crypto.time.IntervalConfig;
import java.util.Date;
import javax.annotation.Nullable;
import org.bouncycastle.cert.ocsp.CertificateStatus;
import org.bouncycastle.cert.ocsp.RevokedStatus;

public class CredentialConfigImpl extends AbstractCredentialDescriptor<KeyPairConfig, IntervalConfig, CertificateConfig> implements CredentialConfig {
    private OcspRevokeReasonType revocationReason;
    private Date revocationTime;

    public CredentialConfigImpl(KeyPairConfig keyPairDescriptor, CertificateConfig certDescriptor) {
        super(keyPairDescriptor, certDescriptor);
    }

    @Nullable
    @Override
    public CertificateStatus getCertificateStatus() {
        if (!this.hasRevocationReason()) {
            return CertificateStatus.GOOD;
        }

        if (this.revocationTime == null) {
            this.revocationTime = new Date();
        }

        return new RevokedStatus(this.revocationTime, this.revocationReason.getTag());
    }

    @Override
    public boolean hasRevocationReason() {
        return (this.revocationReason != null);
    }

    @Nullable
    @Override
    public OcspRevokeReasonType getRevocationReason() {
        return this.revocationReason;
    }

    @Override
    public void setRevocationReason(@Nullable OcspRevokeReasonType revocationReason) {
        this.revocationReason = revocationReason;
    }

    @Override
    public boolean hasRevocationTime() {
        return (this.revocationTime != null);
    }

    @Nullable
    @Override
    public Date getRevocationTime() {
        return this.revocationTime;
    }

    @Override
    public void setRevocationTime(@Nullable Date revocationTime) {
        this.revocationTime = revocationTime;
    }
}
