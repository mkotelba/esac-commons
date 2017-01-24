package com.esacinc.commons.build.maven;

import com.esacinc.commons.utils.EsacStringUtils;
import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;

public final class EsacMavenProperties {
    public final static String NAME_PREFIX = "esac-commons.build.maven.";

    public final static String BASEDIR_NAME = "basedir";
    public final static String MOJO_EXEC_NAME = "mojoExecution";
    public final static String PLUGIN_NAME = "plugin";
    public final static String PROJECT_NAME = "project";
    public final static String QUIET_NAME = "quiet";
    public final static String SESSION_NAME = "session";
    public final static String SETTINGS_NAME = "settings";
    public final static String VERBOSE_NAME = "verbose";

    public final static String MOJO_EXEC_VALUE_PREFIX = PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + MOJO_EXEC_NAME;
    public final static String PLUGIN_VALUE_PREFIX = PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + PLUGIN_NAME;
    public final static String PROJECT_VALUE_PREFIX = PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + PROJECT_NAME;
    public final static String SESSION_VALUE_PREFIX = PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + SESSION_NAME;
    public final static String SETTINGS_VALUE_PREFIX = PlaceholderConfigurerSupport.DEFAULT_PLACEHOLDER_PREFIX + SETTINGS_NAME;

    public final static String MOJO_EXEC_VALUE = MOJO_EXEC_VALUE_PREFIX + EsacStringUtils.R_BRACE;
    public final static String PLUGIN_VALUE = PLUGIN_VALUE_PREFIX + EsacStringUtils.R_BRACE;
    public final static String PROJECT_VALUE = PROJECT_VALUE_PREFIX + EsacStringUtils.R_BRACE;
    public final static String PROJECT_BASEDIR_VALUE = PROJECT_VALUE_PREFIX + EsacStringUtils.PERIOD + BASEDIR_NAME + EsacStringUtils.R_BRACE;
    public final static String SESSION_VALUE = SESSION_VALUE_PREFIX + EsacStringUtils.R_BRACE;
    public final static String SETTINGS_VALUE = SETTINGS_VALUE_PREFIX + EsacStringUtils.R_BRACE;

    private EsacMavenProperties() {
    }
}
