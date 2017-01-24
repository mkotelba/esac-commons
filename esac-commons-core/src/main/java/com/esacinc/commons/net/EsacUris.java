package com.esacinc.commons.net;

import com.esacinc.commons.utils.EsacStringUtils;
import java.net.URI;
import org.springframework.util.ResourceUtils;

public final class EsacUris {
    public final static String HIERARCHICAL_DELIM = "://";

    public final static String ESAC_COMMONS_URL_PREFIX = EsacUris.HTTP_URL_PREFIX + "dev.esacinc.com/commons/";
    public final static String HTTP_URL_PREFIX = EsacSchemes.HTTP + HIERARCHICAL_DELIM;
    public final static String JAR_FILE_URL_PREFIX = ResourceUtils.JAR_URL_PREFIX + ResourceUtils.FILE_URL_PREFIX;
    public final static String URN_PREFIX = EsacSchemes.URN + EsacStringUtils.COLON;

    public final static String ESAC_COMMONS_DATA_DB_CACHE_URL_VALUE = ESAC_COMMONS_URL_PREFIX + "data-db-cache";
    public final static URI ESAC_COMMONS_DATA_DB_CACHE_URL = URI.create(ESAC_COMMONS_DATA_DB_CACHE_URL_VALUE);

    private EsacUris() {
    }
}
