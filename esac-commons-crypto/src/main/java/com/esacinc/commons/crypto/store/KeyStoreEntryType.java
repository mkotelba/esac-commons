package com.esacinc.commons.crypto.store;

import com.esacinc.commons.beans.TypedBean;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStore.TrustedCertificateEntry;

public enum KeyStoreEntryType implements TypedBean {
    PRIVATE_KEY(PrivateKeyEntry.class), TRUSTED_CERTIFICATE(TrustedCertificateEntry.class), SECRET_KEY(SecretKeyEntry.class);

    private final Class<?> type;

    private KeyStoreEntryType(Class<?> type) {
        this.type = type;
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }
}
