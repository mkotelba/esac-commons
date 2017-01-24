package com.esacinc.commons.beans.factory.impl;

import org.springframework.beans.factory.SmartFactoryBean;

public abstract class AbstractEsacFactoryBean<T> implements SmartFactoryBean<T> {
    protected Class<T> objClass;
    protected boolean eagerInit = true;
    protected boolean prototype;

    protected AbstractEsacFactoryBean(Class<T> objClass) {
        this.objClass = objClass;
    }

    @Override
    public boolean isEagerInit() {
        return this.eagerInit;
    }

    public void setEagerInit(boolean eagerInit) {
        this.eagerInit = eagerInit;
    }

    @Override
    public Class<?> getObjectType() {
        return this.objClass;
    }

    @Override
    public boolean isPrototype() {
        return this.prototype;
    }

    public void setPrototype(boolean prototype) {
        this.prototype = prototype;
    }

    @Override
    public boolean isSingleton() {
        return !this.isPrototype();
    }
}
