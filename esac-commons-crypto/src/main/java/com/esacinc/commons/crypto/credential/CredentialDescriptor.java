package com.esacinc.commons.crypto.credential;

import com.esacinc.commons.beans.DescriptorBean;
import com.esacinc.commons.crypto.cert.CertificateDescriptor;
import com.esacinc.commons.crypto.key.KeyPairDescriptor;
import com.esacinc.commons.crypto.time.IntervalDescriptor;

public interface CredentialDescriptor<T extends KeyPairDescriptor, U extends IntervalDescriptor, V extends CertificateDescriptor<U>> extends DescriptorBean {
    public V getCertificateDescriptor();

    public T getKeyPairDescriptor();
}
