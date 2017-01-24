package com.esacinc.commons.data.db.cache;

import com.esacinc.commons.beans.NamedBean;
import org.ehcache.config.CacheConfiguration;

public interface EsacCacheConfiguration<T, U> extends CacheConfiguration<T, U>, NamedBean {
}
