package com.esacinc.commons.tx.impl;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

public class TxTaskExecutor extends CustomizableThreadFactory implements Executor {
    private final static long serialVersionUID = 0L;

    private Map<String, TxIdGenerator> txIdGens;

    public TxTaskExecutor(Map<String, TxIdGenerator> txIdGens) {
        this.txIdGens = txIdGens;
    }

    public Future<?> submit(Runnable task) {
        return Executors.newSingleThreadExecutor(this).submit(((Runnable) new TxTaskWrapper<>(this.txIdGens, Executors.callable(task, null))));
    }

    public <T> Future<T> submit(Callable<T> task) {
        return Executors.newSingleThreadExecutor(this).submit(((Callable<T>) new TxTaskWrapper<>(this.txIdGens, task)));
    }

    @Override
    public void execute(Runnable task) {
        Executors.newSingleThreadExecutor(this).execute(new TxTaskWrapper<>(this.txIdGens, Executors.callable(task, null)));
    }
}
