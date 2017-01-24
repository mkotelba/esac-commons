package com.esacinc.commons.concurrent.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFutureTask;

public class ThreadPoolTaskExecutorService extends ThreadPoolTaskExecutor implements ExecutorService {
    private final static long serialVersionUID = 0L;

    @Override
    public List<Runnable> shutdownNow() {
        return this.getThreadPoolExecutor().shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return this.getThreadPoolExecutor().isShutdown();
    }

    @Override
    public boolean awaitTermination(@Nonnegative long timeout, TimeUnit timeUnit) throws InterruptedException {
        return this.getThreadPoolExecutor().awaitTermination(timeout, timeUnit);
    }

    @Override
    public boolean isTerminated() {
        return this.getThreadPoolExecutor().isTerminated();
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <T> List<Future<T>> invokeAll(Callable<T> ... tasks) throws ExecutionException, InterruptedException, TimeoutException {
        return this.invokeAll(Stream.of(tasks).collect(Collectors.toList()));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.getThreadPoolExecutor().invokeAll(tasks);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <T> List<Future<T>> invokeAll(@Nonnegative long timeout, TimeUnit timeUnit, Callable<T> ... tasks)
        throws ExecutionException, InterruptedException, TimeoutException {
        return this.invokeAll(Stream.of(tasks).collect(Collectors.toList()), timeout, timeUnit);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, @Nonnegative long timeout, TimeUnit timeUnit) throws InterruptedException {
        return this.getThreadPoolExecutor().invokeAll(tasks, timeout, timeUnit);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <T> T invokeAny(Callable<T> ... tasks) throws ExecutionException, InterruptedException, TimeoutException {
        return this.invokeAny(Stream.of(tasks).collect(Collectors.toList()));
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws ExecutionException, InterruptedException {
        return this.getThreadPoolExecutor().invokeAny(tasks);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <T> T invokeAny(@Nonnegative long timeout, TimeUnit timeUnit, Callable<T> ... tasks)
        throws ExecutionException, InterruptedException, TimeoutException {
        return this.invokeAny(Stream.of(tasks).collect(Collectors.toList()), timeout, timeUnit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, @Nonnegative long timeout, TimeUnit timeUnit)
        throws ExecutionException, InterruptedException, TimeoutException {
        return this.getThreadPoolExecutor().invokeAny(tasks, timeout, timeUnit);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <T, U extends ListenableFutureTask<T>> List<U> submitListenables(U ... tasks) {
        return Stream.of(tasks).map(this::submitListenable).collect(Collectors.toList());
    }

    public <T, U extends ListenableFutureTask<T>> U submitListenable(U task) {
        this.getThreadPoolExecutor().execute(task);

        return task;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return this.getThreadPoolExecutor().submit(task, result);
    }
}
