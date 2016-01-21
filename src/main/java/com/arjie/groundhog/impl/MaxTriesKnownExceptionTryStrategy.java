package com.arjie.groundhog.impl;

import com.arjie.groundhog.TryStrategy;

import java.util.Collection;

public class MaxTriesKnownExceptionTryStrategy<S extends NumTriesAndExceptionTracker> implements TryStrategy<S> {

  private final long maxTries;
  private final Collection<Class<? extends Exception>> exceptionsToRetry;

  public MaxTriesKnownExceptionTryStrategy(long maxTries, Collection<Class<? extends Exception>> exceptionsToRetry) {
    this.maxTries = maxTries;
    this.exceptionsToRetry = exceptionsToRetry;
  }

  @Override
  public boolean shouldTry(S state) {

    if (state.getNumTries() >= maxTries) {
      return false;
    }

    Exception exception = state.getLastException();
    return (exception == null) || (isExceptionCatchable(exception));

  }

  private boolean isExceptionCatchable(Exception exception) {
    for (Class<? extends Exception> x : exceptionsToRetry) {
      if (x.isAssignableFrom(exception.getClass())) {
        return true;
      }
    }
    return false;
  }
}
