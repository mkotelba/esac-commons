package com.esacinc.commons.beans.factory;

import com.esacinc.commons.beans.DescriptorBean;

public interface FactoryComponent<T extends DescriptorBean> {
    public T getDescriptor();
}
