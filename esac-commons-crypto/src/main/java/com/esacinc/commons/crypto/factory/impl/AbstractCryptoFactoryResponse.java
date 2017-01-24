package com.esacinc.commons.crypto.factory.impl;

import com.esacinc.commons.beans.factory.impl.AbstractFactoryResponse;
import com.esacinc.commons.crypto.CryptoInfo;
import com.esacinc.commons.crypto.factory.CryptoFactoryResponse;

public abstract class AbstractCryptoFactoryResponse<T extends CryptoInfo> extends AbstractFactoryResponse<T> implements CryptoFactoryResponse<T> {
}
