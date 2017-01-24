package com.esacinc.commons.data.db.jdbc.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.metrics.MetricsTracker;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.PoolStats;
import java.sql.Driver;
import java.util.Properties;
import org.springframework.beans.factory.InitializingBean;

public class EsacDataSourceConfig extends HikariConfig implements InitializingBean, MetricsTrackerFactory {
    private class EsacMetricsTracker extends MetricsTracker {
        
    }
    
    private final static String PASSWORD_DATA_SRC_PROP_NAME = "password";
    private final static String USER_DATA_SRC_PROP_NAME = "user";
    
    private Driver driver;
    
    @Override
    public MetricsTracker create(String poolName, PoolStats poolStats) {
        return new EsacMetricsTracker();
    }

    public EsacDriverDataSource buildDataSource(boolean singleConn) {
        Properties props = new Properties();
        props.putAll(this.getDataSourceProperties());
        props.put(PASSWORD_DATA_SRC_PROP_NAME, this.getPassword());
        props.put(USER_DATA_SRC_PROP_NAME, this.getUsername());

        return new EsacDriverDataSource(this.driver, this.getJdbcUrl(), props, singleConn);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.driver = ((Driver) Class.forName(this.getDriverClassName()).newInstance());

        this.setDataSource(this.buildDataSource(false));

        this.setMetricsTrackerFactory(this);
    }
}
