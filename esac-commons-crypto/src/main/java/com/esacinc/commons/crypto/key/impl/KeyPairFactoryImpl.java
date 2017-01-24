package com.esacinc.commons.crypto.key.impl;

import com.esacinc.commons.crypto.CryptoException;
import com.esacinc.commons.crypto.factory.impl.AbstractCryptoServiceFactory;
import com.esacinc.commons.crypto.key.KeyPairConfig;
import com.esacinc.commons.crypto.key.KeyPairFactory;
import com.esacinc.commons.crypto.key.KeyPairFactoryRequest;
import com.esacinc.commons.crypto.key.KeyPairFactoryResponse;
import com.esacinc.commons.crypto.key.KeyPairInfo;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Provider;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyPairFactoryImpl extends AbstractCryptoServiceFactory<KeyPairConfig, KeyPairFactoryRequest, KeyPair, KeyPairInfo, KeyPairFactoryResponse>
    implements KeyPairFactory {
    private final static Logger LOGGER = LoggerFactory.getLogger(KeyPairFactoryImpl.class);

    @Override
    public KeyPairInfo build(KeyPairConfig config) throws Exception {
        return this.build(new KeyPairFactoryRequestImpl(config)).getDescriptor();
    }

    @Override
    protected KeyPairFactoryResponse buildInternal(KeyPairFactoryRequest req, KeyPairConfig config, @Nullable String id, @Nullable String name)
        throws Exception {
        Provider prov = config.getProvider();
        String algName = config.getAlgorithmName();

        KeyPairGenerator gen = KeyPairGenerator.getInstance(algName, prov);
        gen.initialize(config.getSize(), config.getSecureRandom());

        KeyPairInfo info = new KeyPairInfoImpl(gen.generateKeyPair());
        info.setId(id);
        info.setName(name);

        KeyPairFactoryResponse resp = super.buildInternal(req, config, id, name);
        resp.setDescriptor(info);

        // noinspection ConstantConditions
        LOGGER.info(String.format("Processed key pair factory request (provName=%s, algOid=%s, algName=%s, id=%s, name=%s).", prov.getName(),
            config.getAlgorithmId().getAlgorithm(), algName, id, name));

        return resp;
    }

    @Override
    protected KeyPairFactoryResponse buildResponse() throws Exception {
        return new KeyPairFactoryResponseImpl();
    }

    @Override
    protected void validate(KeyPairFactoryRequest req, KeyPairConfig config, @Nullable String id, @Nullable String name) throws Exception {
        if (!config.hasSize()) {
            throw new CryptoException(String.format("Invalid key pair factory request (provName=%s, algOid=%s, algName=%s, id=%s, name=%s) key size: %d",
                config.getProvider().getName(), config.getAlgorithmId().getAlgorithm(), config.getAlgorithmName(), config.getId(), config.getName(),
                config.getSize()));
        }
    }
}
