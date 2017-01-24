package com.esacinc.commons.crypto.cert.impl;

import com.esacinc.commons.crypto.cert.CertificateConfig;
import com.esacinc.commons.crypto.cert.CertificateFactoryRequest;
import com.esacinc.commons.crypto.credential.CredentialInfo;
import com.esacinc.commons.crypto.factory.impl.AbstractCryptoFactoryRequest;
import com.esacinc.commons.crypto.key.KeyPairInfo;
import javax.annotation.Nullable;

public class CertificateFactoryRequestImpl extends AbstractCryptoFactoryRequest<CertificateConfig> implements CertificateFactoryRequest {
    private CredentialInfo issuerCredInfo;
    private KeyPairInfo keyPairInfo;

    public CertificateFactoryRequestImpl(@Nullable CredentialInfo issuerCredInfo, KeyPairInfo keyPairInfo, CertificateConfig descriptor) {
        super(descriptor);

        this.issuerCredInfo = issuerCredInfo;
        this.keyPairInfo = keyPairInfo;
    }

    @Override
    public boolean hasIssuerCredentialInfo() {
        return (this.issuerCredInfo != null);
    }

    @Nullable
    @Override
    public CredentialInfo getIssuerCredentialInfo() {
        return this.issuerCredInfo;
    }

    @Override
    public KeyPairInfo getKeyPairInfo() {
        return this.keyPairInfo;
    }
}
