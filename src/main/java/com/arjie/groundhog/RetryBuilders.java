package com.arjie.groundhog;

import java.util.Collection;
import java.util.concurrent.Callable;

import com.arjie.groundhog.impl.ExponentialDelayStrategy;
import com.arjie.groundhog.impl.FixedDelayStrategy;
import com.arjie.groundhog.impl.MaxTriesKnownExceptionTryStrategy;
import com.arjie.groundhog.impl.NumTriesAndExceptionTracker;

public class RetryBuilders {

  /**
   * Create a retrier that will retry a given {@link Callable} up to a fixed maximum number of times,
   * waiting a fixed time in between, and catching only some {@link Exception}s.
   * A re-attempt will be made only if the previous attempt failed.
   *
   * @param maxTries The maximum number of attempts to make.
   * @param waitMillisBetweenTries The time in milliseconds to wait between attempts.
   * @param exceptionsToRetryOn The exceptions to catch and retry on. Others will be propagated up the chain normally.
   *
   * @return A {@link RetryBuilder} that works as described above.
   */
  public static RetryBuilder<NumTriesAndExceptionTracker> fixedTriesFixedDelay(int maxTries, long waitMillisBetweenTries, Collection<Class<? extends Exception>> exceptionsToRetryOn) {
    return new RetryBuilder<>(new NumTriesAndExceptionTracker.Factory())
        .withTryStrategy(new MaxTriesKnownExceptionTryStrategy<>(maxTries, exceptionsToRetryOn))
        .withDelayStrategy(new FixedDelayStrategy<>(waitMillisBetweenTries));
  }

  /**
   * Create a retrier that will retry given {@link Callable} up to a fixed maximum number of times,
   * exponentially backing off, and catching only some {@link Exception}s.
   * A re-attempt will be made only if the previous attempt failed.
   *
   * @param maxTries The maximum number of attempts to make.
   * @param delayFactor The value to multiply the previous delay by to get the current delay
   * @param initialDelayInMillis The initial delay in milliseconds
   * @param exceptionsToRetryOn The exceptions to catch and retry on. Others will be propagated up the chain normally.
   *
   * @return A {@link Callable} that works as described above.
   */
  public static RetryBuilder<NumTriesAndExceptionTracker> fixedTriesExponentialBackoff(int maxTries, double delayFactor, long initialDelayInMillis, Collection<Class<? extends Exception>> exceptionsToRetryOn) {
    return new RetryBuilder<>(new NumTriesAndExceptionTracker.Factory())
        .withTryStrategy(new MaxTriesKnownExceptionTryStrategy<>(maxTries, exceptionsToRetryOn))
        .withDelayStrategy(new ExponentialDelayStrategy<NumTriesAndExceptionTracker>(delayFactor, initialDelayInMillis));
  }

}
