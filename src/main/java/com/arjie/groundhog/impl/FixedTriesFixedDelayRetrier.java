package com.arjie.groundhog.impl;

import com.arjie.groundhog.Retrier;

import java.util.Collection;
import java.util.concurrent.Callable;

public class FixedTriesFixedDelayRetrier<V> extends Retrier<V, NumTriesAndExceptionTracker> {
  public FixedTriesFixedDelayRetrier(Callable<V> c, int maxTries, long waitMillisBetweenTries, Collection<Class<? extends Exception>> exceptionsToRetryOn) {
    super(c, new FixedDelayMaxTriesKnownExceptionTryStrategy<>(maxTries, waitMillisBetweenTries, exceptionsToRetryOn), new NumTriesAndExceptionTracker.Factory());
  }
}
