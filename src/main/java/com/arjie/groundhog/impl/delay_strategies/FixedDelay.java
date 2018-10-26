package com.arjie.groundhog.impl.delay_strategies;

import com.arjie.groundhog.DelayStrategy;
import com.arjie.groundhog.TryState;

public class FixedDelay<S extends TryState> implements DelayStrategy<S> {

  private final long millisToDelay;

  public FixedDelay(long millisToDelay) {
    this.millisToDelay = millisToDelay;
  }


  @Override
  public long getMillisToDelayRetry(TryState state) {
    return millisToDelay;
  }
}
