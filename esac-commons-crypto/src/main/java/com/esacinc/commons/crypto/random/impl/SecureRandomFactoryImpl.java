package com.esacinc.commons.crypto.random.impl;

import com.esacinc.commons.crypto.factory.impl.AbstractCryptoServiceFactory;
import com.esacinc.commons.crypto.random.SecureRandomConfig;
import com.esacinc.commons.crypto.random.SecureRandomFactory;
import com.esacinc.commons.crypto.random.SecureRandomFactoryRequest;
import com.esacinc.commons.crypto.random.SecureRandomFactoryResponse;
import com.esacinc.commons.crypto.random.SecureRandomInfo;
import java.security.SecureRandom;
import javax.annotation.Nullable;

public class SecureRandomFactoryImpl
    extends AbstractCryptoServiceFactory<SecureRandomConfig, SecureRandomFactoryRequest, SecureRandom, SecureRandomInfo, SecureRandomFactoryResponse>
    implements SecureRandomFactory {
    @Override
    public SecureRandom build(SecureRandomConfig config) throws Exception {
        return this.build(new SecureRandomFactoryRequestImpl(config)).getDescriptor().getService();
    }

    @Override
    protected SecureRandomFactoryResponse buildInternal(SecureRandomFactoryRequest req, SecureRandomConfig config, @Nullable String id, @Nullable String name)
        throws Exception {
        SecureRandom service = SecureRandom.getInstance(config.getAlgorithmName(), config.getProvider());
        service.nextBytes(new byte[1]);

        SecureRandomFactoryResponse resp = super.buildInternal(req, config, id, name);
        resp.setDescriptor(new SecureRandomInfoImpl(service));

        return resp;
    }

    @Override
    protected SecureRandomFactoryResponse buildResponse() throws Exception {
        return new SecureRandomFactoryResponseImpl();
    }
}
