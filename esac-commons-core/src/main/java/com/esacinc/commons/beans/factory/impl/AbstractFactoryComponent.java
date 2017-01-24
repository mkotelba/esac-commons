package com.esacinc.commons.beans.factory.impl;

import com.esacinc.commons.beans.DescriptorBean;
import com.esacinc.commons.beans.factory.FactoryComponent;

public abstract class AbstractFactoryComponent<T extends DescriptorBean> implements FactoryComponent<T> {
    protected T descriptor;

    @Override
    public T getDescriptor() {
        return this.descriptor;
    }
}
