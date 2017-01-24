package com.esacinc.commons.crypto.store;

import com.esacinc.commons.beans.DescriptorBean;
import java.security.cert.X509Certificate;

public interface KeyStoreEntryDescriptorBean extends DescriptorBean {
    public String getAlias();

    public X509Certificate getCertificate();

    public KeyStoreEntryType getType();
}
