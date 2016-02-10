package com.arjie.groundhog.impl;

import com.arjie.groundhog.DelayStrategy;
import com.arjie.groundhog.TryState;

/**
 * Delay each execution by {@link #factor} times the previous delay, starting with {@link #initial}.
 * Not meant to be exact for large values.
 *
 * @param <S> The type of state, which must have some way to obtain the number of tries.
 */
public class ExponentialDelayStrategy<S extends NumTriesState & TryState> implements DelayStrategy<S> {

  private final double factor;
  private final long initial;

  public ExponentialDelayStrategy(double factor) {
    this(factor, 1L);
  }

  public ExponentialDelayStrategy(double factor, long initial) {
    this.factor = factor;
    this.initial = initial;
  }

  @Override
  public long getMillisToDelayRetry(S state) {
    return Math.round(initial * (Math.pow(factor, state.getNumTries() - 1)));
  }
}
