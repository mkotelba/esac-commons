package com.esacinc.commons.context.impl;

import com.esacinc.commons.validation.constraints.ValidFile;
import java.io.File;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.io.support.ResourcePatternResolver;

public abstract class AbstractEsacApplication extends SpringApplication {
    public static interface EsacWebApplicationValidationGroup {
    }

    public final static String PROP_SRC_NAME = "appProps";

    public final static String BEAN_NAME = "app";

    protected String groupName;
    protected String propNamePrefix;
    protected File binDir;
    protected String cmd;
    protected File confDir;
    protected File dataDir;
    protected File homeDir;
    protected String instanceId;
    protected File libDir;
    protected boolean loggingConsoleThreadName;
    protected boolean loggingConsoleTty;
    protected boolean loggingConsoleTx;
    protected String loggingFileName;
    protected String loggingLogstashFileName;
    protected Map<String, String> loggingTxMdcMappings;
    protected File logDir;
    protected String name;
    protected long pid;
    protected CompositePropertySource propSrc;
    protected ResourcePatternResolver resourcePatternResolver;
    protected File runDir;
    protected File webappDir;
    protected File workDir;

    protected AbstractEsacApplication(String groupName, String propNamePrefix) {
        this.groupName = groupName;
        this.propNamePrefix = propNamePrefix;
    }

    @ValidFile(directory = true)
    public File getBinDirectory() {
        return this.binDir;
    }

    public void setBinDirectory(File binDir) {
        this.binDir = binDir;
    }

    public String getCommand() {
        return this.cmd;
    }

    public void setCommand(String cmd) {
        this.cmd = cmd;
    }

    @ValidFile(directory = true)
    public File getConfDirectory() {
        return this.confDir;
    }

    public void setConfDirectory(File confDir) {
        this.confDir = confDir;
    }

    @ValidFile(directory = true)
    public File getDataDirectory() {
        return this.dataDir;
    }

    public void setDataDirectory(File dataDir) {
        this.dataDir = dataDir;
    }

    public String getGroupName() {
        return this.groupName;
    }

    @ValidFile(directory = true)
    public File getHomeDirectory() {
        return this.homeDir;
    }

    public void setHomeDirectory(File homeDir) {
        this.homeDir = homeDir;
    }

    public String getInstanceId() {
        return this.instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @ValidFile(directory = true)
    public File getLibDirectory() {
        return this.libDir;
    }

    public void setLibDirectory(File libDir) {
        this.libDir = libDir;
    }

    public boolean isLoggingConsoleThreadName() {
        return this.loggingConsoleThreadName;
    }

    public void setLoggingConsoleThreadName(boolean loggingConsoleThreadName) {
        this.loggingConsoleThreadName = loggingConsoleThreadName;
    }

    public boolean isLoggingConsoleTty() {
        return this.loggingConsoleTty;
    }

    public void setLoggingConsoleTty(boolean loggingConsoleTty) {
        this.loggingConsoleTty = loggingConsoleTty;
    }

    public boolean isLoggingConsoleTx() {
        return this.loggingConsoleTx;
    }

    public void setLoggingConsoleTx(boolean loggingConsoleTx) {
        this.loggingConsoleTx = loggingConsoleTx;
    }

    public String getLoggingFileName() {
        return this.loggingFileName;
    }

    public void setLoggingFileName(String loggingFileName) {
        this.loggingFileName = loggingFileName;
    }

    public String getLoggingLogstashFileName() {
        return this.loggingLogstashFileName;
    }

    public void setLoggingLogstashFileName(String loggingLogstashFileName) {
        this.loggingLogstashFileName = loggingLogstashFileName;
    }

    public Map<String, String> getLoggingTxMdcMappings() {
        return this.loggingTxMdcMappings;
    }

    public void setLoggingTxMdcMappings(Map<String, String> loggingTxMdcMappings) {
        this.loggingTxMdcMappings = loggingTxMdcMappings;
    }

    @ValidFile(directory = true)
    public File getLogDirectory() {
        return this.logDir;
    }

    public void setLogDirectory(File logDir) {
        this.logDir = logDir;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPid() {
        return this.pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getPropertyNamePrefix() {
        return this.propNamePrefix;
    }

    public CompositePropertySource getPropertySource() {
        return this.propSrc;
    }

    public void setPropertySource(CompositePropertySource propSrc) {
        this.propSrc = propSrc;
    }

    public ResourcePatternResolver getResourcePatternResolver() {
        return this.resourcePatternResolver;
    }

    public void setResourcePatternResolver(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    @ValidFile(directory = true)
    public File getRunDirectory() {
        return this.runDir;
    }

    public void setRunDirectory(File runDir) {
        this.runDir = runDir;
    }

    @ValidFile(directory = true, groups = { EsacWebApplicationValidationGroup.class })
    public File getWebappDirectory() {
        return this.webappDir;
    }

    public void setWebappDirectory(File webappDir) {
        this.webappDir = webappDir;
    }

    @ValidFile(directory = true)
    public File getWorkDirectory() {
        return this.workDir;
    }

    public void setWorkDirectory(File workDir) {
        this.workDir = workDir;
    }
}
