package com.esacinc.commons.crypto.random;

import com.esacinc.commons.crypto.factory.CryptoServiceFactory;
import java.security.SecureRandom;

public interface SecureRandomFactory
    extends CryptoServiceFactory<SecureRandomConfig, SecureRandomFactoryRequest, SecureRandom, SecureRandomInfo, SecureRandomFactoryResponse> {
    public SecureRandom build(SecureRandomConfig config) throws Exception;
}
