package com.esacinc.commons.test.context.impl;

import com.esacinc.commons.context.EsacApplicationValue;
import com.esacinc.commons.context.impl.AbstractEsacApplication;
import com.esacinc.commons.test.context.EsacCommonsPropertyNames;
import com.esacinc.commons.utils.EsacStringUtils;
import java.io.File;
import java.util.Map;
import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;

public class EsacCommonsApplication extends AbstractEsacApplication {
    public final static String GROUP_NAME = "esac-commons";
    public final static String PROP_NAME_PREFIX = GROUP_NAME + EsacStringUtils.PERIOD;

    public EsacCommonsApplication(String groupName, String propNamePrefix) {
        super(groupName, propNamePrefix);
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.APP_BIN_DIR +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public File getBinDirectory() {
        return super.getBinDirectory();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.APP_CMD +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public String getCommand() {
        return super.getCommand();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.APP_CONF_DIR +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public File getConfDirectory() {
        return super.getConfDirectory();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.APP_DATA_DIR +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public File getDataDirectory() {
        return super.getDataDirectory();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.APP_HOME_DIR +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public File getHomeDirectory() {
        return super.getHomeDirectory();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.APP_INSTANCE_ID +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public String getInstanceId() {
        return super.getInstanceId();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.APP_LIB_DIR +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public File getLibDirectory() {
        return super.getLibDirectory();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.LOGGING_CONSOLE_THREAD_NAME +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public boolean isLoggingConsoleThreadName() {
        return super.isLoggingConsoleThreadName();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.LOGGING_CONSOLE_TTY +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public boolean isLoggingConsoleTty() {
        return super.isLoggingConsoleTty();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.LOGGING_CONSOLE_TX +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public boolean isLoggingConsoleTx() {
        return super.isLoggingConsoleTx();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.LOGGING_FILE_NAME +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public String getLoggingFileName() {
        return super.getLoggingFileName();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.LOGGING_LOGSTASH_FILE_NAME +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public String getLoggingLogstashFileName() {
        return super.getLoggingLogstashFileName();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.LOGGING_TX_MDC_MAPPINGS +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public Map<String, String> getLoggingTxMdcMappings() {
        return super.getLoggingTxMdcMappings();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.APP_LOG_DIR +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public File getLogDirectory() {
        return super.getLogDirectory();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.APP_NAME +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public String getName() {
        return super.getName();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.APP_PID +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public long getPid() {
        return super.getPid();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.APP_RUN_DIR +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public File getRunDirectory() {
        return super.getRunDirectory();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.APP_WEBAPP_DIR +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public File getWebappDirectory() {
        return super.getWebappDirectory();
    }

    @EsacApplicationValue((PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + EsacCommonsPropertyNames.APP_WORK_DIR +
        PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_SUFFIX))
    @Override
    public File getWorkDirectory() {
        return super.getWorkDirectory();
    }
}
