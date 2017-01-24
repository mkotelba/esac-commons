package com.esacinc.commons.crypto.credential.impl;

import com.esacinc.commons.crypto.cert.CertificateInfo;
import com.esacinc.commons.crypto.credential.CredentialInfo;
import com.esacinc.commons.crypto.key.KeyPairInfo;
import com.esacinc.commons.crypto.time.IntervalInfo;

public class CredentialInfoImpl extends AbstractCredentialDescriptor<KeyPairInfo, IntervalInfo, CertificateInfo> implements CredentialInfo {
    public CredentialInfoImpl(KeyPairInfo keyPairDescriptor, CertificateInfo certDescriptor) {
        super(keyPairDescriptor, certDescriptor);
    }
}
