package com.esacinc.commons.beans.factory;

import com.esacinc.commons.beans.InfoBean;

public interface FactoryResponse<T extends InfoBean> extends FactoryComponent<T> {
    public void setDescriptor(T descriptor);
}
