package com.esacinc.commons.crypto.store.impl;

import com.esacinc.commons.crypto.store.KeyStoreEntryType;
import com.esacinc.commons.crypto.store.PrivateKeyKeyStoreEntryInfo;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import javax.annotation.Nullable;

public class PrivateKeyKeyStoreEntryInfoImpl extends AbstractKeyStoreEntryInfoBean<PrivateKeyEntry> implements PrivateKeyKeyStoreEntryInfo {
    public PrivateKeyKeyStoreEntryInfoImpl(String alias, @Nullable Date creationDate, PrivateKeyEntry entry) {
        super(KeyStoreEntryType.PRIVATE_KEY, alias, creationDate, entry);
    }

    @Override
    public X509Certificate getCertificate() {
        return ((X509Certificate) this.entry.getCertificate());
    }

    @Override
    public X509Certificate[] getCertificates() {
        return ((X509Certificate[]) this.entry.getCertificateChain());
    }

    @Override
    public PrivateKey getPrivateKey() {
        return this.entry.getPrivateKey();
    }
}
