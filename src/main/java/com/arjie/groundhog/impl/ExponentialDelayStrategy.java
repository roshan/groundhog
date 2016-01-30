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

  private final double base;
  private final double coefficient;

  public ExponentialDelayStrategy(double base) {
    this(base, 1.0f);
  }

  public ExponentialDelayStrategy(double base, double coefficient) {

    this.base = base;
    this.coefficient = coefficient;
  }

  @Override
  public long getMillisToDelayRetry(S state) {
    return Math.round(coefficient * (Math.pow(base, state.getNumTries() - 1)));
  }
}
