package com.esacinc.commons.data.db.cache.impl;

import com.esacinc.commons.net.EsacUris;
import java.net.URI;
import java.util.Properties;
import javax.cache.CacheManager;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.jsr107.EhcacheCachingProvider;

public class EsacEhcacheCachingProvider extends EhcacheCachingProvider {
    private DefaultConfiguration config;

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties props) {
        if (!uri.equals(EsacUris.ESAC_COMMONS_DATA_DB_CACHE_URL)) {
            return super.getCacheManager(uri, classLoader, props);
        }

        return this.getCacheManager(uri, this.config);
    }

    public DefaultConfiguration getConfiguration() {
        return this.config;
    }

    public void setConfiguration(DefaultConfiguration config) {
        this.config = config;
    }
}
