package com.esacinc.commons.ws.client.impl;

import com.esacinc.commons.tx.impl.TxTaskExecutor;
import org.apache.cxf.endpoint.DeferredConduitSelector;

public class EsacConduitSelector extends DeferredConduitSelector {
    private TxTaskExecutor taskExec;

    public EsacConduitSelector(TxTaskExecutor taskExec) {
        super();

        this.taskExec = taskExec;
    }

    public TxTaskExecutor getTaskExecutor() {
        return this.taskExec;
    }
}
