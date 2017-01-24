package com.esacinc.commons.crypto.store.impl;

import com.esacinc.commons.crypto.store.KeyStoreEntryType;
import com.esacinc.commons.crypto.store.TrustedCertificateKeyStoreEntryConfig;
import java.security.cert.X509Certificate;

public class TrustedCertificateKeyStoreEntryConfigImpl extends AbstractKeyStoreEntryConfigBean implements TrustedCertificateKeyStoreEntryConfig {
    private X509Certificate cert;

    public TrustedCertificateKeyStoreEntryConfigImpl(String alias) {
        super(KeyStoreEntryType.TRUSTED_CERTIFICATE, alias);
    }

    @Override
    public X509Certificate getCertificate() {
        return this.cert;
    }

    @Override
    public void setCertificate(X509Certificate cert) {
        this.cert = cert;
    }
}
