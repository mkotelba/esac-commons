package com.esacinc.commons.crypto.store.impl;

import com.esacinc.commons.crypto.store.KeyStoreEntryType;
import com.esacinc.commons.crypto.store.TrustedCertificateKeyStoreEntryInfo;
import java.security.KeyStore.TrustedCertificateEntry;
import java.security.cert.X509Certificate;
import java.util.Date;
import javax.annotation.Nullable;

public class TrustedCertificateKeyStoreEntryInfoImpl extends AbstractKeyStoreEntryInfoBean<TrustedCertificateEntry>
    implements TrustedCertificateKeyStoreEntryInfo {
    public TrustedCertificateKeyStoreEntryInfoImpl(String alias, @Nullable Date creationDate, TrustedCertificateEntry entry) {
        super(KeyStoreEntryType.TRUSTED_CERTIFICATE, alias, creationDate, entry);
    }

    @Override
    public X509Certificate getCertificate() {
        return ((X509Certificate) this.entry.getTrustedCertificate());
    }
}
