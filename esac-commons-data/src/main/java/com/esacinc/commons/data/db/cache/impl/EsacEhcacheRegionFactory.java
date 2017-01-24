package com.esacinc.commons.data.db.cache.impl;

import com.esacinc.commons.data.db.cache.EsacCacheConfiguration;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;
import javax.cache.Caching;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.jsr107.config.ConfigurationElementState;
import org.ehcache.jsr107.config.Jsr107Configuration;
import org.ehcache.spi.service.ServiceCreationConfiguration;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.jcache.JCacheRegionFactory;

public class EsacEhcacheRegionFactory extends JCacheRegionFactory {
    private final static long serialVersionUID = 0L;

    private Map<String, EsacCacheConfiguration<?, ?>> cacheConfigs = new LinkedHashMap<>();

    @Override
    public void start(SessionFactoryOptions sessionFactoryOpts, Properties props) throws CacheException {
        DefaultConfiguration config = new DefaultConfiguration(((ClassLoader) null));
        final Jsr107Configuration jsr107Config =
            new Jsr107Configuration(null, new HashMap<>(), true, ConfigurationElementState.ENABLED, ConfigurationElementState.ENABLED);

        ((EsacEhcacheCachingProvider) Caching.getCachingProvider(props.getProperty(PROVIDER)))
            .setConfiguration(new DefaultConfiguration(Collections.unmodifiableMap(this.cacheConfigs), config.getClassLoader(),
                config.getServiceCreationConfigurations().stream()
                    .map(serviceCreationConfig -> ((serviceCreationConfig instanceof Jsr107Configuration) ? jsr107Config : serviceCreationConfig))
                    .toArray(ServiceCreationConfiguration[]::new)));

        super.start(sessionFactoryOpts, props);
    }

    public Map<String, EsacCacheConfiguration<?, ?>> getCacheConfigs() {
        return this.cacheConfigs;
    }

    public void setCacheConfigs(EsacCacheConfiguration<?, ?> ... cacheConfigs) {
        this.cacheConfigs.clear();

        Stream.of(cacheConfigs).forEach(cacheConfig -> this.cacheConfigs.put(cacheConfig.getName(), cacheConfig));
    }
}
