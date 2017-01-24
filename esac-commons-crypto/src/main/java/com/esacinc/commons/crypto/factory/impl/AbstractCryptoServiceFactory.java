package com.esacinc.commons.crypto.factory.impl;

import com.esacinc.commons.crypto.CryptoServiceConfig;
import com.esacinc.commons.crypto.CryptoServiceInfo;
import com.esacinc.commons.crypto.factory.CryptoFactoryRequest;
import com.esacinc.commons.crypto.factory.CryptoFactoryResponse;
import com.esacinc.commons.crypto.factory.CryptoServiceFactory;

public abstract class AbstractCryptoServiceFactory<T extends CryptoServiceConfig, U extends CryptoFactoryRequest<T>, V, W extends CryptoServiceInfo<V>, X extends CryptoFactoryResponse<W>>
    extends AbstractCryptoFactory<T, U, W, X> implements CryptoServiceFactory<T, U, V, W, X> {
}
