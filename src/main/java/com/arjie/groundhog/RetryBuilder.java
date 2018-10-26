package com.arjie.groundhog;

import java.util.concurrent.Callable;

import com.arjie.groundhog.impl.delay_strategies.FixedDelay;

public class RetryBuilder<S extends TryState> {


  private final TryState.Factory<S> initialStateFactory;
  private TryStrategy<? super S> tryStrategy;
  private DelayStrategy<? super S> delayStrategy;

  public RetryBuilder(TryState.Factory<S> initialStateFactory) {
    this.initialStateFactory = initialStateFactory;
    this.tryStrategy = new TryStrategy.Forever<>();
    this.delayStrategy = new FixedDelay<>(1000);
  }

  /**
   * @param tryStrategy The strategy to determine whether another iteration
   *                    should be made given the current {@link TryState}
   */
  public RetryBuilder<S> withTryStrategy(TryStrategy<? super S> tryStrategy) {
    this.tryStrategy = tryStrategy;
    return this;
  }

  /**
   * @param delayStrategy The strategy to determine how long to wait for the
   *                      next iteration given the current {@link TryState}
   */
  public RetryBuilder<S> withDelayStrategy(DelayStrategy<? super S> delayStrategy) {
    this.delayStrategy = delayStrategy;
    return this;
  }

  /**
   * Decorates a Callable so that it retries according to {@link #tryStrategy}
   * and {@link #delayStrategy} and then returns information about the value
   * and the state which it terminated with. This is useful if you want to
   * inspect the final state. For instance, you may want to report how
   * many tries were made.
   *
   * @param callable The {@link Callable} to decorate.
   * @param <V> The value that the callable returns
   * @return The actually returned value as well as the terminal state
   */
  public <V> Callable<RetryResult<V, S>> annotate(Callable<V> callable) {
    return new Retrier<>(callable, tryStrategy, delayStrategy, initialStateFactory);
  }

  /**
   * As in {@link #annotate(Callable)}, decorates a Callable so that it
   * retries according to {@link #tryStrategy} and {@link #delayStrategy}
   * but only returns the value. This is useful if you only want the retry
   * functionality and aren't concerned with the terminal state.
   *
   * @param callable {@link Callable} to decorate
   * @param <V> The value that the callable returns
   * @return The value that the callable returns in its final iteration
   */
  public <V> Callable<V> build(Callable<V> callable) {
    return new Retrier.Pure<>(annotate(callable));
  }

}
