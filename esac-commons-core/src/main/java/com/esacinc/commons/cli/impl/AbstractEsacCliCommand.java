package com.esacinc.commons.cli.impl;

import com.esacinc.commons.cli.EsacCliCommand;
import com.esacinc.commons.cli.EsacCliCommandOptions;

public abstract class AbstractEsacCliCommand<T extends EsacCliCommandOptions> implements EsacCliCommand<T> {
    protected String name;
    protected T opts;

    protected AbstractEsacCliCommand(String name, T opts) {
        this.name = name;
        this.opts = opts;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public T getOptions() {
        return this.opts;
    }
}
