package com.esacinc.commons.crypto.factory;

import com.esacinc.commons.beans.factory.EsacFactory;
import com.esacinc.commons.crypto.CryptoConfig;
import com.esacinc.commons.crypto.CryptoInfo;

public interface CryptoFactory<T extends CryptoConfig, U extends CryptoFactoryRequest<T>, V extends CryptoInfo, W extends CryptoFactoryResponse<V>>
    extends EsacFactory<T, U, V, W> {
}
