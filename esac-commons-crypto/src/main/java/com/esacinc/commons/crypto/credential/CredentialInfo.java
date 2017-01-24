package com.esacinc.commons.crypto.credential;

import com.esacinc.commons.crypto.cert.CertificateInfo;
import com.esacinc.commons.crypto.key.KeyPairInfo;
import com.esacinc.commons.crypto.time.IntervalInfo;

public interface CredentialInfo extends CredentialDescriptor<KeyPairInfo, IntervalInfo, CertificateInfo> {
}
