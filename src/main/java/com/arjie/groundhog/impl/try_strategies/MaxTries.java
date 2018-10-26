package com.arjie.groundhog.impl.try_strategies;

import java.util.ArrayList;
import java.util.Collection;

import com.arjie.groundhog.TryStrategy;
import com.arjie.groundhog.impl.NumTries;

public class MaxTries<S extends NumTries> implements TryStrategy<S> {

  private final long maxTries;
  private final Collection<Class<? extends Exception>> exceptionsToRetry;

  public MaxTries(long maxTries, Collection<Class<? extends Exception>> exceptionsToRetry) {
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

  public static class Builder<T extends NumTries> {
    private long maxTries;
    private Collection<Class<? extends Exception>> exceptionsToRetry;

    public Builder() {
      this.maxTries = 1;
      this.exceptionsToRetry = new ArrayList<>();
    }

    public Builder<T> setMaxTries(long maxTries) {
      this.maxTries = maxTries;
      return this;
    }

    public Builder<T> setExceptionsToRetry(Collection<Class<? extends Exception>> exceptionsToRetry) {
      this.exceptionsToRetry = exceptionsToRetry;
      return this;
    }

    public Builder<T> addExceptionToRetryOn(Class<? extends Exception> exception) {
      this.exceptionsToRetry.add(exception);
      return this;
    }

    public MaxTries<T> build() {
      return new MaxTries<>(maxTries, exceptionsToRetry);
    }
  }
}
