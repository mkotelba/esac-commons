package com.esacinc.commons.beans.factory.impl;

import com.esacinc.commons.beans.ConfigBean;
import com.esacinc.commons.beans.InfoBean;
import com.esacinc.commons.beans.factory.FactoryRequest;
import com.esacinc.commons.beans.factory.FactoryResponse;
import com.esacinc.commons.beans.factory.EsacFactory;
import javax.annotation.Nullable;

public abstract class AbstractEsacFactory<T extends ConfigBean, U extends FactoryRequest<T>, V extends InfoBean, W extends FactoryResponse<V>>
    implements EsacFactory<T, U, V, W> {
    @Override
    public W build(U req) throws Exception {
        T config = req.getDescriptor();
        String id = config.getId(), name = config.getName();

        this.validate(req, config, id, name);

        return this.buildInternal(req, config, id, name);
    }

    protected W buildInternal(U req, T config, @Nullable String id, @Nullable String name) throws Exception {
        return this.buildResponse();
    }

    protected abstract W buildResponse() throws Exception;

    protected void validate(U req, T config, @Nullable String id, @Nullable String name) throws Exception {
    }
}
