package com.esacinc.commons.test.context;

import com.esacinc.commons.context.EsacPropertyNames;
import com.esacinc.commons.test.context.impl.EsacCommonsApplication;
import com.esacinc.commons.utils.EsacStringUtils;

public final class EsacCommonsPropertyNames {
    public final static String PREFIX = EsacCommonsApplication.GROUP_NAME + EsacStringUtils.PERIOD;

    public final static String APP_BIN_DIR = PREFIX + EsacPropertyNames.APP_BIN_DIR;
    public final static String APP_CMD = PREFIX + EsacPropertyNames.APP_CMD;
    public final static String APP_CONF_DIR = PREFIX + EsacPropertyNames.APP_CONF_DIR;
    public final static String APP_DATA_DIR = PREFIX + EsacPropertyNames.APP_DATA_DIR;
    public final static String APP_HOME_DIR = PREFIX + EsacPropertyNames.APP_HOME_DIR;
    public final static String APP_INSTANCE_ID = PREFIX + EsacPropertyNames.APP_INSTANCE_ID;
    public final static String APP_LIB_DIR = PREFIX + EsacPropertyNames.APP_LIB_DIR;
    public final static String APP_LOG_DIR = PREFIX + EsacPropertyNames.APP_LOG_DIR;
    public final static String APP_NAME = PREFIX + EsacPropertyNames.APP_NAME;
    public final static String APP_PID = PREFIX + EsacPropertyNames.APP_PID;
    public final static String APP_RUN_DIR = PREFIX + EsacPropertyNames.APP_RUN_DIR;
    public final static String APP_WEBAPP_DIR = PREFIX + EsacPropertyNames.APP_WEBAPP_DIR;
    public final static String APP_WORK_DIR = PREFIX + EsacPropertyNames.APP_WORK_DIR;
    public final static String HTTP_CLIENT_TX_ID = PREFIX + EsacPropertyNames.HTTP_CLIENT_TX_ID;
    public final static String HTTP_SERVER_TX_ID = PREFIX + EsacPropertyNames.HTTP_SERVER_TX_ID;
    public final static String LOGGING_CONSOLE_THREAD_NAME = PREFIX + EsacPropertyNames.LOGGING_CONSOLE_THREAD_NAME;
    public final static String LOGGING_CONSOLE_TTY = PREFIX + EsacPropertyNames.LOGGING_CONSOLE_TTY;
    public final static String LOGGING_CONSOLE_TX = PREFIX + EsacPropertyNames.LOGGING_CONSOLE_TX;
    public final static String LOGGING_FILE_NAME = PREFIX + EsacPropertyNames.LOGGING_FILE_NAME;
    public final static String LOGGING_LOGSTASH_FILE_NAME = PREFIX + EsacPropertyNames.LOGGING_LOGSTASH_FILE_NAME;
    public final static String LOGGING_TX_MDC_MAPPINGS = PREFIX + EsacPropertyNames.LOGGING_TX_MDC_MAPPINGS;

    private EsacCommonsPropertyNames() {
    }
}
