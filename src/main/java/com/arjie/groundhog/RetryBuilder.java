package com.arjie.groundhog;

import java.util.concurrent.Callable;

import com.arjie.groundhog.impl.FixedDelayStrategy;

public class RetryBuilder<S extends TryState> {


  private final TryState.Factory<S> initialStateFactory;
  private TryStrategy<? super S> tryStrategy;
  private DelayStrategy<? super S> delayStrategy;

  public RetryBuilder(TryState.Factory<S> initialStateFactory) {
    this.initialStateFactory = initialStateFactory;
    this.tryStrategy = new TryStrategy.Forever<>();
    this.delayStrategy = new FixedDelayStrategy<>(1000);
  }

  public RetryBuilder<S> withTryStrategy(TryStrategy<? super S> tryStrategy) {
    this.tryStrategy = tryStrategy;
    return this;
  }

  public RetryBuilder<S> withDelayStrategy(DelayStrategy<? super S> delayStrategy) {
    this.delayStrategy = delayStrategy;
    return this;
  }

  public <V> Callable<RetryResult<V, S>> buildAnnotatedFor(Callable<V> callable) {
    return new Retrier<>(callable, tryStrategy, delayStrategy, initialStateFactory);
  }

  public <V> Callable<V> buildPurelyFor(Callable<V> callable) {
    return new Retrier.Pure<>(buildAnnotatedFor(callable));
  }

}
