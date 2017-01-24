package com.esacinc.commons.crypto.key;

import com.esacinc.commons.crypto.factory.CryptoServiceFactory;
import java.security.KeyPair;

public interface KeyPairFactory extends CryptoServiceFactory<KeyPairConfig, KeyPairFactoryRequest, KeyPair, KeyPairInfo, KeyPairFactoryResponse> {
    public KeyPairInfo build(KeyPairConfig config) throws Exception;
}
