package com.esacinc.commons.context;

import com.esacinc.commons.utils.EsacStringUtils;

public final class EsacPropertyNames {
    public final static String APP_PREFIX = "app.";
    public final static String CLIENT_PREFIX = "client.";
    public final static String FILE_PREFIX = "file.";
    public final static String HTTP_PREFIX = "http.";
    public final static String HTTP_CLIENT_PREFIX = HTTP_PREFIX + CLIENT_PREFIX;
    public final static String HTTP_SERVER_PREFIX = HTTP_PREFIX + EsacPropertyNames.SERVER_PREFIX;
    public final static String LOGGING_PREFIX = "logging.";
    public final static String LOGGING_CONSOLE_PREFIX = LOGGING_PREFIX + "console.";
    public final static String LOGGING_FILE_PREFIX = LOGGING_PREFIX + FILE_PREFIX;
    public final static String LOGGING_LOGGER_PREFIX = LOGGING_PREFIX + "logger.";
    public final static String LOGGING_LOGSTASH_PREFIX = LOGGING_PREFIX + "logstash.";
    public final static String LOGGING_LOGSTASH_FILE_PREFIX = LOGGING_LOGSTASH_PREFIX + FILE_PREFIX;
    public final static String SERVER_PREFIX = "server.";

    public final static String DIR_SUFFIX = "dir";
    public final static String ID_SUFFIX = "id";
    public final static String NAME_SUFFIX = "name";
    public final static String TX_SUFFIX = "tx";
    public final static String TX_ID_SUFFIX = TX_SUFFIX + EsacStringUtils.PERIOD + ID_SUFFIX;

    public final static String APP_BIN_DIR = APP_PREFIX + "bin." + DIR_SUFFIX;
    public final static String APP_CMD = APP_PREFIX + "cmd";
    public final static String APP_CONF_DIR = APP_PREFIX + "conf." + DIR_SUFFIX;
    public final static String APP_DATA_DIR = APP_PREFIX + "data." + DIR_SUFFIX;
    public final static String APP_HOME_DIR = APP_PREFIX + "home." + DIR_SUFFIX;
    public final static String APP_INSTANCE_ID = APP_PREFIX + "instance." + ID_SUFFIX;
    public final static String APP_LIB_DIR = APP_PREFIX + "lib." + DIR_SUFFIX;
    public final static String APP_LOG_DIR = APP_PREFIX + "log." + DIR_SUFFIX;
    public final static String APP_NAME = APP_PREFIX + NAME_SUFFIX;
    public final static String APP_PID = APP_PREFIX + "pid";
    public final static String APP_RUN_DIR = APP_PREFIX + "run." + DIR_SUFFIX;
    public final static String APP_WEBAPP_DIR = APP_PREFIX + "webapp." + DIR_SUFFIX;
    public final static String APP_WORK_DIR = APP_PREFIX + "work." + DIR_SUFFIX;
    public final static String HTTP_CLIENT_TX_ID = HTTP_CLIENT_PREFIX + TX_ID_SUFFIX;
    public final static String HTTP_SERVER_TX_ID = HTTP_SERVER_PREFIX + TX_ID_SUFFIX;
    public final static String LOGGING_CONSOLE_THREAD_NAME = LOGGING_CONSOLE_PREFIX + "thread." + NAME_SUFFIX;
    public final static String LOGGING_CONSOLE_TTY = LOGGING_CONSOLE_PREFIX + "tty";
    public final static String LOGGING_CONSOLE_TX = LOGGING_CONSOLE_PREFIX + TX_SUFFIX;
    public final static String LOGGING_FILE_NAME = LOGGING_FILE_PREFIX + NAME_SUFFIX;
    public final static String LOGGING_LOGSTASH_FILE_NAME = LOGGING_LOGSTASH_FILE_PREFIX + NAME_SUFFIX;
    public final static String LOGGING_TX_MDC_MAPPINGS = LOGGING_PREFIX + TX_SUFFIX + ".mdc.mappings";

    private EsacPropertyNames() {
    }
}
