package com.esacinc.commons.data.db.jdbc.impl;

import com.zaxxer.hikari.HikariDataSource;
import com.esacinc.commons.data.db.jdbc.EsacDataSourceInitializer;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class EsacDataSource extends HikariDataSource implements DisposableBean, InitializingBean {
    @Autowired(required = false)
    private List<EsacDataSourceInitializer> initializers;

    private EsacDataSourceConfig config;

    public EsacDataSource(EsacDataSourceConfig config) {
        super();

        this.config = config;
    }

    @Override
    public void destroy() throws Exception {
        this.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.config.validate();
        this.config.copyState(this);

        if (!CollectionUtils.isEmpty(this.initializers)) {
            for (EsacDataSourceInitializer initializer : this.initializers) {
                initializer.initializeDataSource(this);
            }
        }

        this.getConnection().close();
    }
}
