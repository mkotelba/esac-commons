package com.esacinc.commons.beans.factory.impl;

import com.esacinc.commons.beans.InfoBean;
import com.esacinc.commons.beans.factory.FactoryResponse;

public abstract class AbstractFactoryResponse<T extends InfoBean> extends AbstractFactoryComponent<T> implements FactoryResponse<T> {
    @Override
    public void setDescriptor(T descriptor) {
        this.descriptor = descriptor;
    }
}
