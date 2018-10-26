package com.arjie.groundhog.impl.delay_strategies;

import com.arjie.groundhog.DelayStrategy;
import com.arjie.groundhog.impl.NumTries;

/**
 * Delay each execution by {@link #factor} times the previous delay, starting with {@link #initial}.
 * Not meant to be exact for large values.
 *
 * @param <S> The type of state, which must have some way to obtain the number of tries.
 */
public class ExponentialDelay<S extends NumTries> implements DelayStrategy<S> {

  private final double factor;
  private final long initial;

  public ExponentialDelay(double factor) {
    this(factor, 1L);
  }

  public ExponentialDelay(double factor, long initial) {
    this.factor = factor;
    this.initial = initial;
  }

  @Override
  public long getMillisToDelayRetry(S state) {
    return Math.round(initial * (Math.pow(factor, state.getNumTries() - 1)));
  }
}
