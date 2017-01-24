package com.esacinc.commons.crypto.factory;

import com.esacinc.commons.crypto.CryptoServiceConfig;
import com.esacinc.commons.crypto.CryptoServiceInfo;

public interface CryptoServiceFactory<T extends CryptoServiceConfig, U extends CryptoFactoryRequest<T>, V, W extends CryptoServiceInfo<V>, X extends CryptoFactoryResponse<W>>
    extends CryptoFactory<T, U, W, X> {
}
