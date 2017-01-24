package com.esacinc.commons.logging.elasticsearch;

public final class ElasticsearchFieldNames {
    public final static String CLIENT_PREFIX = "client.";
    public final static String HTTP_PREFIX = "http.";
    public final static String HTTP_CLIENT_PREFIX = HTTP_PREFIX + CLIENT_PREFIX;
    public final static String HTTP_SERVER_PREFIX = HTTP_PREFIX + ElasticsearchFieldNames.SERVER_PREFIX;
    public final static String SERVER_PREFIX = "server.";
    public final static String SSL_PREFIX = "ssl.";
    public final static String SSL_CLIENT_PREFIX = SSL_PREFIX + CLIENT_PREFIX;
    public final static String SSL_SERVER_PREFIX = SSL_PREFIX + SERVER_PREFIX;
    public final static String WS_PREFIX = "ws.";
    public final static String WS_CLIENT_PREFIX = WS_PREFIX + CLIENT_PREFIX;
    public final static String WS_SERVER_PREFIX = WS_PREFIX + SERVER_PREFIX;

    public final static String HELLO_SUFFIX = "hello";
    public final static String ID_SUFFIX = "id";
    public final static String REQUEST_SUFFIX = "request";
    public final static String RESPONSE_SUFFIX = "response";
    public final static String TRUST_SUFFIX = "trust";

    public final static String CONTENT = "content";
    public final static String DN = "dn";
    public final static String END = "end";
    public final static String INTERVAL = "interval";
    public final static String ISSUER = "issuer";
    public final static String NAME = "name";
    public final static String SERIAL_NUM = "serial_number";
    public final static String SUBJECT = "subject";
    public final static String START = "start";
    public final static String VALUES = "values";
    public final static String VERSION = "version";

    private ElasticsearchFieldNames() {
    }
}
