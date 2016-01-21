package com.arjie.groundhog.impl;

import com.arjie.groundhog.DelayStrategy;
import com.arjie.groundhog.TryState;

/**
 * Delay each execution by {@link #base} times the previous delay starting with {@link #coefficient}.
 * Not meant to be exact for large values.
 *
 * @param <S> The type of state, which must have some way to obtain the number of tries.
 */
public class ExponentialDelayStrategy<S extends NumTriesState & TryState> implements DelayStrategy<S> {

  private final long base;
  private final long coefficient;

  public ExponentialDelayStrategy(long base) {
    this(base, 1);
  }

  public ExponentialDelayStrategy(long base, long coefficient) {

    this.base = base;
    this.coefficient = coefficient;
  }

  @Override
  public long getMillisToDelayRetry(S state) {
    return Math.round(coefficient * (Math.pow(base, state.getNumTries() - 1)));
  }
}
