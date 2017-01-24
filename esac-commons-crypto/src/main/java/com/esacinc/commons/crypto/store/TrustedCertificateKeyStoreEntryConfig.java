package com.esacinc.commons.crypto.store;

import java.security.cert.X509Certificate;

public interface TrustedCertificateKeyStoreEntryConfig extends KeyStoreEntryConfigBean, TrustedCertificateKeyStoreEntryDescriptorBean {
    public void setCertificate(X509Certificate cert);
}
