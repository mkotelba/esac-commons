package com.esacinc.commons.concurrent.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.concurrent.ListenableFutureTask;

public class ThreadPoolTaskSchedulerService extends ThreadPoolTaskScheduler implements ScheduledExecutorService {
    private final static long serialVersionUID = 0L;

    @Override
    public List<Runnable> shutdownNow() {
        return this.getScheduledThreadPoolExecutor().shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return this.getScheduledThreadPoolExecutor().isShutdown();
    }

    @Override
    public boolean awaitTermination(@Nonnegative long timeout, TimeUnit timeUnit) throws InterruptedException {
        return this.getScheduledThreadPoolExecutor().awaitTermination(timeout, timeUnit);
    }

    @Override
    public boolean isTerminated() {
        return this.getScheduledThreadPoolExecutor().isTerminated();
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, @Nonnegative long initialDelay, @Nonnegative long delay, TimeUnit timeUnit) {
        return this.getScheduledThreadPoolExecutor().scheduleWithFixedDelay(task, initialDelay, delay, timeUnit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, @Nonnegative long initialDelay, @Nonnegative long period, TimeUnit timeUnit) {
        return this.getScheduledThreadPoolExecutor().scheduleAtFixedRate(task, initialDelay, period, timeUnit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> task, @Nonnegative long delay, TimeUnit timeUnit) {
        return this.getScheduledThreadPoolExecutor().schedule(task, delay, timeUnit);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable task, @Nonnegative long delay, TimeUnit timeUnit) {
        return this.getScheduledThreadPoolExecutor().schedule(task, delay, timeUnit);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <T> List<Future<T>> invokeAll(Callable<T> ... tasks) throws ExecutionException, InterruptedException, TimeoutException {
        return this.invokeAll(Stream.of(tasks).collect(Collectors.toList()));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.getScheduledThreadPoolExecutor().invokeAll(tasks);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <T> List<Future<T>> invokeAll(@Nonnegative long timeout, TimeUnit timeUnit, Callable<T> ... tasks)
        throws ExecutionException, InterruptedException, TimeoutException {
        return this.invokeAll(Stream.of(tasks).collect(Collectors.toList()), timeout, timeUnit);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, @Nonnegative long timeout, TimeUnit timeUnit) throws InterruptedException {
        return this.getScheduledThreadPoolExecutor().invokeAll(tasks, timeout, timeUnit);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <T> T invokeAny(Callable<T> ... tasks) throws ExecutionException, InterruptedException, TimeoutException {
        return this.invokeAny(Stream.of(tasks).collect(Collectors.toList()));
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws ExecutionException, InterruptedException {
        return this.getScheduledThreadPoolExecutor().invokeAny(tasks);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <T> T invokeAny(@Nonnegative long timeout, TimeUnit timeUnit, Callable<T> ... tasks)
        throws ExecutionException, InterruptedException, TimeoutException {
        return this.invokeAny(Stream.of(tasks).collect(Collectors.toList()), timeout, timeUnit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, @Nonnegative long timeout, TimeUnit timeUnit)
        throws ExecutionException, InterruptedException, TimeoutException {
        return this.getScheduledThreadPoolExecutor().invokeAny(tasks, timeout, timeUnit);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <T, U extends ListenableFutureTask<T>> List<U> submitListenables(U ... tasks) {
        return Stream.of(tasks).map(this::submitListenable).collect(Collectors.toList());
    }

    public <T, U extends ListenableFutureTask<T>> U submitListenable(U task) {
        this.getScheduledThreadPoolExecutor().execute(task);

        return task;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return this.getScheduledThreadPoolExecutor().submit(task, result);
    }
}
