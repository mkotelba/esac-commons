package com.esacinc.commons.json.impl;

import com.esacinc.commons.beans.factory.impl.AbstractEsacFactoryBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.esacinc.commons.json.ObjectMapperConfig;

public class ObjectMapperFactoryBean extends AbstractEsacFactoryBean<ObjectMapper> {
    private ObjectMapperConfig config = new ObjectMapperConfigImpl();

    public ObjectMapperFactoryBean() {
        super(ObjectMapper.class);
    }

    @Override
    public ObjectMapper getObject() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();

        this.config.configure(objMapper);

        return objMapper;
    }

    public ObjectMapperConfig getConfig() {
        return this.config;
    }

    public void setConfig(ObjectMapperConfig config) {
        this.config.merge(config);
    }
}
