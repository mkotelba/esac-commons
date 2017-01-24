package com.esacinc.commons.crypto.credential.impl;

import com.esacinc.commons.beans.impl.AbstractDescriptorBean;
import com.esacinc.commons.crypto.cert.CertificateDescriptor;
import com.esacinc.commons.crypto.credential.CredentialDescriptor;
import com.esacinc.commons.crypto.key.KeyPairDescriptor;
import com.esacinc.commons.crypto.time.IntervalDescriptor;

public abstract class AbstractCredentialDescriptor<T extends KeyPairDescriptor, U extends IntervalDescriptor, V extends CertificateDescriptor<U>>
    extends AbstractDescriptorBean implements CredentialDescriptor<T, U, V> {
    protected T keyPairDescriptor;
    protected V certDescriptor;

    protected AbstractCredentialDescriptor(T keyPairDescriptor, V certDescriptor) {
        super(null, null);

        this.keyPairDescriptor = keyPairDescriptor;
        this.certDescriptor = certDescriptor;
    }

    @Override
    public V getCertificateDescriptor() {
        return this.certDescriptor;
    }

    @Override
    public T getKeyPairDescriptor() {
        return this.keyPairDescriptor;
    }
}
