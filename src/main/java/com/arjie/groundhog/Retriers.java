package com.arjie.groundhog;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;

import com.arjie.groundhog.impl.NumTriesAndExceptionTracker;

@SuppressWarnings("unused")
public class Retriers {

  /**
   * Retry given {@link Callable} up to a fixed maximum number of times, waiting a fixed time in between, and catching
   * only some {@link Exception}s. A re-attempt will be made only if the previous attempt failed.
   *
   * @param <V> The return value of the {@link Callable}.
   *
   * @param c The {@link Callable} to retry.
   * @param maxTries The maximum number of attempts to make.
   * @param waitMillisBetweenTries The time in milliseconds to wait between attempts.
   * @param exceptionsToRetryOn The exceptions to catch and retry on. Others will be propagated up the chain normally.
   * @return A {@link Callable} that works as described above.
   */
  public static <V> Callable<RetryResult<V, NumTriesAndExceptionTracker>> fixedTriesFixedDelay(Callable<V> c, int maxTries, long waitMillisBetweenTries, Collection<Class<? extends Exception>> exceptionsToRetryOn) {
    return RetryBuilders.fixedTriesFixedDelay(maxTries, waitMillisBetweenTries, exceptionsToRetryOn).buildAnnotatedFor(c);
  }

  /**
   * Catch all {@link Exception} but otherwise behave like {@link #fixedTriesFixedDelay(Callable, int, long, Collection)}
   *
   * @param <V> The return value of the {@link Callable}.
   *
   * @param c The {@link Callable} to retry.
   * @param maxTries The maximum number of attempts to make.
   * @param waitMillisBetweenTries The time in milliseconds to wait between attempts.
   * @return A {@link Callable} that works as described above.
   */
  public static <V> Callable<RetryResult<V, NumTriesAndExceptionTracker>> fixedTriesFixedDelay(Callable<V> c, int maxTries, long waitMillisBetweenTries) {
    return fixedTriesFixedDelay(c, maxTries, waitMillisBetweenTries, Collections.<Class<? extends Exception>>singletonList(Exception.class));
  }

  /**
   * Retry given {@link Callable} up to a fixed maximum number of times, exponentially backing off, and catching only
   * some {@link Exception}s. A re-attempt will be made only if the previous attempt failed.
   *
   * @param <V> The return value of the {@link Callable}.
   *
   * @param c The {@link Callable} to retry
   * @param maxTries The maximum number of attempts to make.
   * @param delayFactor The value to multiply the previous delay by to get the current delay
   * @param initialDelayInMillis The initial delay in milliseconds
   * @param exceptionsToRetryOn The exceptions to catch and retry on. Others will be propagated up the chain normally.
   * @return A {@link Callable} that works as described above.
   */
  public static <V> Callable<RetryResult<V, NumTriesAndExceptionTracker>> fixedTriesExponentialBackoff(Callable<V> c, int maxTries, double delayFactor, long initialDelayInMillis, Collection<Class<? extends Exception>> exceptionsToRetryOn) {
    return RetryBuilders.fixedTriesExponentialBackoff(maxTries, delayFactor, initialDelayInMillis, exceptionsToRetryOn).buildAnnotatedFor(c);
  }

  /**
   * Catch all {@link Exception} but otherwise behave like {@link #fixedTriesExponentialBackoff(Callable, int, double, long, Collection)}
   *
   * @param <V> The return value of the {@link Callable}.
   *
   * @param c The {@link Callable} to retry
   * @param maxTries The maximum number of attempts to make.
   * @param delayFactor The value to multiply the previous delay by to get the current delay
   * @param initialDelayInMillis The initial delay in milliseconds
   * @return A {@link Callable} that works as described above.
   */
  public static <V> Callable<RetryResult<V, NumTriesAndExceptionTracker>> fixedTriesExponentialBackoff(Callable<V> c, int maxTries, double delayFactor, long initialDelayInMillis) {
    return fixedTriesExponentialBackoff(c, maxTries, delayFactor, initialDelayInMillis, Collections.<Class<? extends Exception>>singletonList(Exception.class));
  }
}
