package com.esacinc.commons.crypto.store.impl;

import com.esacinc.commons.crypto.store.KeyStoreEntryType;
import com.esacinc.commons.crypto.store.PrivateKeyKeyStoreEntryConfig;
import java.security.KeyStore.PasswordProtection;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class PrivateKeyKeyStoreEntryConfigImpl extends AbstractKeyStoreEntryConfigBean implements PrivateKeyKeyStoreEntryConfig {
    private PasswordProtection protection;
    private X509Certificate[] certs;
    private PrivateKey privateKey;

    public PrivateKeyKeyStoreEntryConfigImpl(String alias, String pass) {
        super(KeyStoreEntryType.PRIVATE_KEY, alias);

        this.protection = new PasswordProtection(pass.toCharArray());
    }

    @Override
    public X509Certificate getCertificate() {
        return this.certs[0];
    }

    @Override
    public X509Certificate[] getCertificates() {
        return this.certs;
    }

    @Override
    public void setCertificates(X509Certificate ... certs) {
        this.certs = certs;
    }

    @Override
    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    @Override
    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public PasswordProtection getProtection() {
        return this.protection;
    }
}
