package com.arjie.groundhog.impl.try_strategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import com.arjie.groundhog.TryStrategy;
import com.arjie.groundhog.impl.NumTries;

public class MaxTries<S extends NumTries> implements TryStrategy<S> {

  private final long maxTries;
  private Predicate<Exception> canRetry;


  public MaxTries(long maxTries, Collection<Class<? extends Exception>> exceptionsToRetry) {
    this(maxTries, exception -> isExceptionCatchable(exceptionsToRetry, exception));
  }

  public MaxTries(long maxTries, Predicate<Exception> canRetry) {
    this.maxTries = maxTries;
    this.canRetry = canRetry;
  }

  @Override
  public boolean shouldTry(S state) {

    if (state.getNumTries() >= maxTries) {
      return false;
    }

    Exception lastException = state.getLastException();
    return lastException == null || canRetry.test(lastException);
  }

  private static boolean isExceptionCatchable(Collection<Class<? extends Exception>> exceptionsToRetry, Exception exception) {
    for (Class<? extends Exception> x : exceptionsToRetry) {
      if (x.isAssignableFrom(exception.getClass())) {
        return true;
      }
    }
    return exceptionsToRetry.isEmpty();
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

    public MaxTries<T> retryAll() {
      return new MaxTries<T>(maxTries, exception -> true);
    }

    public MaxTries<T> build() {
      return new MaxTries<>(maxTries, exceptionsToRetry);
    }
  }
}
