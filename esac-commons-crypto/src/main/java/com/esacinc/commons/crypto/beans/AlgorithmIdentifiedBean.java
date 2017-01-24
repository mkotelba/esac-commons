package com.esacinc.commons.crypto.beans;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface AlgorithmIdentifiedBean {
    public AlgorithmIdentifier getAlgorithmId();
}
