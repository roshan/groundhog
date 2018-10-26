package com.arjie.groundhog;

import java.util.concurrent.Callable;

import com.arjie.groundhog.impl.delay_strategies.ExponentialDelay;
import com.arjie.groundhog.impl.delay_strategies.FixedDelay;
import com.arjie.groundhog.impl.try_strategies.MaxTries;
import com.arjie.groundhog.impl.NumTries;

public class RetryBuilders {

  /**
   * @return A retrier that retries forever with a fixed delay
   */
  public static RetryBuilder<NumTries> basic() {
    return new RetryBuilder<>(new NumTries.Factory());
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
  public static RetryBuilder<NumTries> fixedTriesFixedDelay(int maxTries, long waitMillisBetweenTries) {
    return basic()
        .withTryStrategy(new MaxTries.Builder<>().setMaxTries(maxTries).build())
        .withDelayStrategy(new FixedDelay<>(waitMillisBetweenTries));
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
  public static RetryBuilder<NumTries> fixedTriesExponentialBackoff(int maxTries, double delayFactor, long initialDelayInMillis) {
    return basic()
        .withTryStrategy(new MaxTries.Builder<>().setMaxTries(maxTries).build())
        .withDelayStrategy(new ExponentialDelay<>(delayFactor, initialDelayInMillis));
  }

}
