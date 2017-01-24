package com.esacinc.commons.data.db.cache.impl;

import com.esacinc.commons.data.db.cache.EsacCacheConfiguration;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnegative;
import org.ehcache.config.Eviction;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.core.config.BaseCacheConfiguration;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;

public class EsacCacheConfigurationImpl<T, U> extends BaseCacheConfiguration<T, U> implements EsacCacheConfiguration<T, U> {
    private String name;

    public EsacCacheConfigurationImpl(String name, Class<T> keyType, Class<U> valueType, @Nonnegative long expireDuration, @Nonnegative long maxSize) {
        super(keyType, valueType, Eviction.noAdvice(), null, Expirations.timeToLiveExpiration(Duration.of(expireDuration, TimeUnit.MILLISECONDS)),
            ResourcePoolsBuilder.heap(maxSize).build());

        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
