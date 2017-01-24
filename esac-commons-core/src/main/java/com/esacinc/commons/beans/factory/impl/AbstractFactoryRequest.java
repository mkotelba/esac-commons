package com.esacinc.commons.beans.factory.impl;

import com.esacinc.commons.beans.ConfigBean;
import com.esacinc.commons.beans.factory.FactoryRequest;

public abstract class AbstractFactoryRequest<T extends ConfigBean> extends AbstractFactoryComponent<T> implements FactoryRequest<T> {
    protected AbstractFactoryRequest(T descriptor) {
        this.descriptor = descriptor;
    }
}
