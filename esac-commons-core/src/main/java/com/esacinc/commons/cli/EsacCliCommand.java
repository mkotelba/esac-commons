package com.esacinc.commons.cli;

import com.esacinc.commons.beans.NamedBean;

public interface EsacCliCommand<T extends EsacCliCommandOptions> extends NamedBean {
    public void execute() throws Exception;

    public T getOptions();
}
