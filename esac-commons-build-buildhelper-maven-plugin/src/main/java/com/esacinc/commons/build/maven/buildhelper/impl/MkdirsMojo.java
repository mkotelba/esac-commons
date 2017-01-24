package com.esacinc.commons.build.maven.buildhelper.impl;

import com.esacinc.commons.build.maven.EsacMavenProperties;
import com.esacinc.commons.build.maven.buildhelper.BuildhelperMavenProperties;
import java.io.File;
import java.util.List;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(defaultPhase = LifecyclePhase.INITIALIZE, name = "mkdirs")
public class MkdirsMojo extends AbstractBuildhelperMojo {
    private final static String PROP_NAME_PREFIX = BuildhelperMavenProperties.NAME_PREFIX + "mkdirs.";

    @Parameter(defaultValue = EsacMavenProperties.PROJECT_BASEDIR_VALUE, name = "baseDirectory", property = (PROP_NAME_PREFIX + "dir.base"))
    private File baseDir;

    @Parameter(name = "directories", property = (PROP_NAME_PREFIX + "dirs"), required = true)
    private List<String> dirPaths;

    @Parameter(property = (PROP_NAME_PREFIX + EsacMavenProperties.QUIET_NAME))
    private boolean quiet;

    @Parameter(property = (PROP_NAME_PREFIX + EsacMavenProperties.VERBOSE_NAME))
    private boolean verbose;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = this.getLog();
        boolean verbose = (!this.quiet && this.verbose);

        for (String dirPath : this.dirPaths) {
            this.makeDirectory(log, verbose, new File(dirPath));
        }
    }

    private void makeDirectory(Log log, boolean verbose, File dir) throws MojoExecutionException, MojoFailureException {
        if (!dir.isAbsolute()) {
            dir = new File(this.baseDir, dir.getPath());
        }

        if (dir.exists()) {
            if (!dir.isDirectory()) {
                throw new MojoFailureException(String.format("Existing directory path is not a directory: %s", dir));
            }

            if (verbose) {
                log.info(String.format("Skipped existing directory: %s", dir));
            }
        } else {
            this.makeDirectory(log, verbose, dir.getParentFile());

            if (!dir.mkdir()) {
                throw new MojoFailureException(String.format("Unable to create directory: %s", dir));
            }

            if (!this.quiet) {
                log.info(String.format("Created directory: %s", dir));
            }
        }
    }

    public File getBaseDirectory() {
        return this.baseDir;
    }

    public void setBaseDirectory(File baseDir) {
        this.baseDir = baseDir;
    }

    public List<String> getDirectories() {
        return this.dirPaths;
    }

    public void setDirectories(List<String> dirPaths) {
        this.dirPaths = dirPaths;
    }

    public boolean isQuiet() {
        return this.quiet;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    public boolean isVerbose() {
        return this.verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
