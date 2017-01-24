package com.esacinc.commons.crypto.store;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public interface PrivateKeyKeyStoreEntryDescriptorBean extends KeyStoreEntryDescriptorBean {
    public X509Certificate[] getCertificates();

    public PrivateKey getPrivateKey();
}
