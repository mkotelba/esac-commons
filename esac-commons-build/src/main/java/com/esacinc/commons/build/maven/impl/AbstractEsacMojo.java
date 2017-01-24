package com.esacinc.commons.build.maven.impl;

import com.esacinc.commons.build.maven.EsacMavenProperties;
import java.io.File;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;

public abstract class AbstractEsacMojo extends AbstractMojo {
    @Parameter(defaultValue = EsacMavenProperties.MOJO_EXEC_VALUE, name = "mojoExecution", readonly = true)
    private MojoExecution mojoExec;

    @Parameter(defaultValue = EsacMavenProperties.PLUGIN_VALUE, name = "pluginDescriptor", readonly = true)
    private PluginDescriptor pluginDesc;

    @Parameter(defaultValue = EsacMavenProperties.PROJECT_VALUE, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = EsacMavenProperties.PROJECT_BASEDIR_VALUE, name = "projectBaseDirectory", readonly = true)
    private File projectBaseDir;

    @Parameter(defaultValue = EsacMavenProperties.SESSION_VALUE, readonly = true)
    private MavenSession session;

    @Parameter(defaultValue = EsacMavenProperties.SETTINGS_VALUE, readonly = true)
    private Settings settings;

    public MojoExecution getMojoExecution() {
        return this.mojoExec;
    }

    public void setMojoExecution(MojoExecution mojoExec) {
        this.mojoExec = mojoExec;
    }

    public PluginDescriptor getPluginDescriptor() {
        return this.pluginDesc;
    }

    public void setPluginDescriptor(PluginDescriptor pluginDesc) {
        this.pluginDesc = pluginDesc;
    }

    public MavenProject getProject() {
        return this.project;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public File getProjectBaseDirectory() {
        return this.projectBaseDir;
    }

    public void setProjectBaseDirectory(File projectBaseDir) {
        this.projectBaseDir = projectBaseDir;
    }

    public MavenSession getSession() {
        return this.session;
    }

    public void setSession(MavenSession session) {
        this.session = session;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
