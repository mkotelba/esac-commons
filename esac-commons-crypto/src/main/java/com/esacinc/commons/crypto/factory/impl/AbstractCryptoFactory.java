package com.esacinc.commons.crypto.factory.impl;

import com.esacinc.commons.beans.factory.impl.AbstractEsacFactory;
import com.esacinc.commons.crypto.CryptoConfig;
import com.esacinc.commons.crypto.CryptoInfo;
import com.esacinc.commons.crypto.factory.CryptoFactory;
import com.esacinc.commons.crypto.factory.CryptoFactoryRequest;
import com.esacinc.commons.crypto.factory.CryptoFactoryResponse;

public abstract class AbstractCryptoFactory<T extends CryptoConfig, U extends CryptoFactoryRequest<T>, V extends CryptoInfo, W extends CryptoFactoryResponse<V>>
    extends AbstractEsacFactory<T, U, V, W> implements CryptoFactory<T, U, V, W> {
}
