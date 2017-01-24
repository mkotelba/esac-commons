package com.esacinc.commons.crypto.store;

import java.security.KeyStore.PasswordProtection;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public interface PrivateKeyKeyStoreEntryConfig extends KeyStoreEntryConfigBean, PrivateKeyKeyStoreEntryDescriptorBean {
    public void setCertificates(X509Certificate ... certs);

    public void setPrivateKey(PrivateKey privateKey);

    public PasswordProtection getProtection();
}
