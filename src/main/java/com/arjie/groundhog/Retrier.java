package com.arjie.groundhog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * A decorator for {@link Callable}s that retries them according to the given strategy.
 *
 * @param <V> The return value of the {@link Callable}
 * @param <S> The kind of state that is used to track retries.
 */
public class Retrier<V, S extends TryState> implements Callable<RetryResult<V, S>> {

  public static final String EXHAUSTED_TRIES_MESSAGE = "Can no longer try.";
  private final Callable<V> c;
  private final TryStrategy<? super S> tryStrategy;
  private final DelayStrategy<? super S> delayStrategy;
  private final TryState.Factory<? extends S> initialStateFactory;


  public Retrier(Callable<V> c, TryStrategy<? super S> tryStrategy, DelayStrategy<? super S> delayStrategy, TryState.Factory<? extends S> initialStateFactory) {
    this.c = c;
    this.tryStrategy = tryStrategy;
    this.delayStrategy = delayStrategy;
    this.initialStateFactory = initialStateFactory;
  }

  /**
   * Repeatedly call the contained {@link Callable#call()} so long as {@link #tryStrategy} determines
   * that more tries should be made.
   *
   * @return A container {@link RetryResult} for the result and state
   * @throws AccumulatedException In case no more tries possible, or interrupted, throw an exception that describes
   * exceptions thrown so far and the current state.
   */
  @Override
  public RetryResult<V, S> call() throws AccumulatedException {
    S state = initialStateFactory.constructInitialState();
    List<Exception> exceptions = new ArrayList<>();
    while (tryStrategy.shouldTry(state)) {

      if (Thread.interrupted()) {
        InterruptedException x = new InterruptedException();
        exceptions.add(x);
        throw new AccumulatedException("Thread was interrupted prematurely.", exceptions, x, state);
      }

      state.prepareForAttempt();
      try {
        V val = c.call();
        return new RetryResult<>(state, val);
      } catch (Exception e) {
        exceptions.add(e);
        state.updateForExceptionThrown(e);

        if (tryStrategy.shouldTry(state)) {
          try {
            Thread.sleep(delayStrategy.getMillisToDelayRetry(state));
          } catch (InterruptedException x) {
            exceptions.add(x);
            throw new AccumulatedException("Thread interrupted while delaying retry.", exceptions, x, state);
          }
        }
      }
    }

    if (exceptions.size() > 0) {
      throw new AccumulatedException(EXHAUSTED_TRIES_MESSAGE, exceptions, exceptions.get(exceptions.size() - 1), state);
    } else {
      throw new AccumulatedException(EXHAUSTED_TRIES_MESSAGE, state);
    }
  }

  static class Pure<V, S> implements Callable<V> {

    private final Callable<RetryResult<V, S>> retryCallable;

    public Pure(Callable<RetryResult<V, S>> retryCallable) {
      this.retryCallable = retryCallable;
    }

    @Override
    public V call() throws Exception {
      return retryCallable.call().getReturnValue();
    }
  }
}
