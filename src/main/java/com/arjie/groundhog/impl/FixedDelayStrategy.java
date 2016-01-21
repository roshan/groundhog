package com.arjie.groundhog.impl;

import com.arjie.groundhog.DelayStrategy;
import com.arjie.groundhog.TryState;

public class FixedDelayStrategy<S extends TryState> implements DelayStrategy<S> {

  private long millisToDelay;

  public FixedDelayStrategy(long millisToDelay) {
    this.millisToDelay = millisToDelay;
  }


  @Override
  public long getMillisToDelayRetry(TryState state) {
    return millisToDelay;
  }
}
