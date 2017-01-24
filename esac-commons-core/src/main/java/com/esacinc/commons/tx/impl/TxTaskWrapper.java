package com.esacinc.commons.tx.impl;

import com.google.common.util.concurrent.UncheckedExecutionException;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;
import org.slf4j.MDC;

public class TxTaskWrapper<T> implements Callable<T>, Runnable {
    private Map<String, TxIdGenerator> txIdGens;
    private Callable<T> task;

    public TxTaskWrapper(Map<String, TxIdGenerator> txIdGens, Callable<T> task) {
        this.txIdGens = txIdGens;
        this.task = task;
    }

    @Override
    public void run() {
        try {
            this.call();
        } catch (Exception e) {
            throw new UncheckedExecutionException(e);
        }
    }

    @Nullable
    @Override
    public T call() throws Exception {
        try {
            long timestamp = System.currentTimeMillis();

            this.txIdGens.forEach((txIdMdcPropName, txIdGen) -> MDC.put(txIdMdcPropName, txIdGen.generateId(timestamp).toString()));

            return this.task.call();
        } finally {
            this.txIdGens.keySet().forEach(MDC::remove);
        }
    }
}
