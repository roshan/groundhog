package com.arjie.groundhog;

import java.util.Collections;
import java.util.concurrent.Callable;

import com.arjie.groundhog.impl.ExponentialDelayStrategy;
import com.arjie.groundhog.impl.FixedDelayStrategy;
import com.arjie.groundhog.impl.MaxTriesKnownExceptionTryStrategy;
import com.arjie.groundhog.impl.NumTriesAndExceptionTracker;

public class RetryBuilders {

  /**
   * @return A retrier that retries forever with a fixed delay
   */
  public static RetryBuilder<NumTriesAndExceptionTracker> basic() {
    return new RetryBuilder<>(new NumTriesAndExceptionTracker.Factory());
  }

  /**
   * Create a retrier that will retry a given {@link Callable} up to a fixed maximum number of times,
   * waiting a fixed time in between, and catching only some {@link Exception}s.
   * A re-attempt will be made only if the previous attempt failed.
   *
   * @param maxTries The maximum number of attempts to make.
   * @param waitMillisBetweenTries The time in milliseconds to wait between attempts.
   *
   * @return A {@link RetryBuilder} that works as described above.
   */
  public static RetryBuilder<NumTriesAndExceptionTracker> fixedTriesFixedDelay(int maxTries, long waitMillisBetweenTries) {
    return basic()
        .withTryStrategy(new MaxTriesKnownExceptionTryStrategy<>(maxTries, Collections.<Class<? extends Exception>>singletonList(Exception.class)))
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
   *
   * @return A {@link Callable} that works as described above.
   */
  public static RetryBuilder<NumTriesAndExceptionTracker> fixedTriesExponentialBackoff(int maxTries, double delayFactor, long initialDelayInMillis) {
    return basic()
        .withTryStrategy(new MaxTriesKnownExceptionTryStrategy<>(maxTries))
        .withDelayStrategy(new ExponentialDelayStrategy<NumTriesAndExceptionTracker>(delayFactor, initialDelayInMillis));
  }

}
