package com.arjie.groundhog.impl;

import com.arjie.groundhog.TryStrategy;

import java.util.Collection;

class FixedDelayMaxTriesKnownExceptionTryStrategy<S extends NumTriesAndExceptionTracker> implements TryStrategy<S> {

  private long maxTries;
  private long delayInMillis;
  private Collection<Class<? extends Exception>> exceptionsToRetry;

  public FixedDelayMaxTriesKnownExceptionTryStrategy(long maxTries, long delayInMillis, Collection<Class<? extends Exception>> exceptionsToRetry) {
    this.maxTries = maxTries;
    this.delayInMillis = delayInMillis;
    this.exceptionsToRetry = exceptionsToRetry;
  }

  @Override
  public boolean shouldTry(S state) {

    if (state.getNumTries() >= maxTries) {
      return false;
    }

    Exception exception = state.getLastException();
    if ((exception == null) || (isExceptionCatchable(exception))) {
      return true;
    }

    return false;
  }

  private boolean isExceptionCatchable(Exception exception) {
    for (Class<? extends Exception> x : exceptionsToRetry) {
      if (x.isAssignableFrom(exception.getClass())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public long getMillisToDelayRetry(S state) {
    return delayInMillis;
  }
}
