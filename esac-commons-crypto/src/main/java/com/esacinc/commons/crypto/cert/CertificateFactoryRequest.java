package com.esacinc.commons.crypto.cert;

import com.esacinc.commons.crypto.credential.CredentialInfo;
import com.esacinc.commons.crypto.factory.CryptoFactoryRequest;
import com.esacinc.commons.crypto.key.KeyPairInfo;
import javax.annotation.Nullable;

public interface CertificateFactoryRequest extends CryptoFactoryRequest<CertificateConfig> {
    public boolean hasIssuerCredentialInfo();

    @Nullable
    public CredentialInfo getIssuerCredentialInfo();

    public KeyPairInfo getKeyPairInfo();
}
