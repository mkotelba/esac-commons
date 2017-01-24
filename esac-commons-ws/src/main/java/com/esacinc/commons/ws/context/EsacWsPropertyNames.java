package com.esacinc.commons.ws.context;

import com.esacinc.commons.context.EsacPropertyNames;

public final class EsacWsPropertyNames {
    public final static String WS_PREFIX = "ws.";
    public final static String WS_CLIENT_PREFIX = WS_PREFIX + EsacPropertyNames.CLIENT_PREFIX;
    public final static String WS_SERVER_PREFIX = WS_PREFIX + EsacPropertyNames.SERVER_PREFIX;

    public final static String WS_CLIENT_TX_ID = WS_CLIENT_PREFIX + EsacPropertyNames.TX_ID_SUFFIX;
    public final static String WS_SERVER_TX_ID = WS_SERVER_PREFIX + EsacPropertyNames.TX_ID_SUFFIX;

    private EsacWsPropertyNames() {
    }
}
