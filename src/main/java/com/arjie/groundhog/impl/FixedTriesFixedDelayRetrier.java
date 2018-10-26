package com.arjie.groundhog.impl;

import com.arjie.groundhog.Retrier;
import com.arjie.groundhog.impl.delay_strategies.FixedDelay;
import com.arjie.groundhog.impl.try_strategies.MaxTries;

import java.util.Collection;
import java.util.concurrent.Callable;

public class FixedTriesFixedDelayRetrier<V> extends Retrier<V, NumTries> {
  public FixedTriesFixedDelayRetrier(Callable<V> c, int maxTries, long waitMillisBetweenTries, Collection<Class<? extends Exception>> exceptionsToRetryOn) {
    super(c, new MaxTries<>(maxTries, exceptionsToRetryOn), new FixedDelay<NumTries>(waitMillisBetweenTries), new NumTries.Factory());
  }
}
