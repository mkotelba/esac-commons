package com.esacinc.commons.cli.impl;

import com.esacinc.commons.cli.EsacCliCommandOptions;
import org.kohsuke.args4j.Option;

public abstract class AbstractEsacCliCommandOptions implements EsacCliCommandOptions {
    protected boolean help;

    @Override
    public boolean isHelp() {
        return this.help;
    }

    @Option(name = "--help", usage = "Show help.", help = true)
    @Override
    public void setHelp(boolean help) {
        this.help = help;
    }
}
