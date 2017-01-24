package com.esacinc.commons.beans.factory;

import com.esacinc.commons.beans.ConfigBean;
import com.esacinc.commons.beans.InfoBean;

public interface EsacFactory<T extends ConfigBean, U extends FactoryRequest<T>, V extends InfoBean, W extends FactoryResponse<V>> {
    public W build(U req) throws Exception;
}
