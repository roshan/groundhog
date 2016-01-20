package com.arjie.groundhog;

import com.arjie.groundhog.impl.NumTriesAndExceptionTracker;
import com.arjie.groundhog.impl.FixedTriesFixedDelayRetrier;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;

public class Retriers {
  public static <V> Retrier<V, NumTriesAndExceptionTracker> fixedTriesFixedDelay(Callable<V> c, int maxTries, long waitMillisBetweenTries) {
    return new FixedTriesFixedDelayRetrier<>(c, maxTries, waitMillisBetweenTries, Collections.<Class<? extends Exception>>singletonList(Exception.class));
  }

  /**
   * Retry given {@link Callable} up to a fixed maximum number of times, waiting a fixed time in between, and catching
   * only some {@link Exception}s. An attempt will be made only if the previous attempt failed.
   *
   * @param c The {@link Callable} to retry.
   * @param maxTries The maximum number of attempts to make.
   * @param waitMillisBetweenTries The time in milliseconds to wait between attempts.
   * @param exceptionsToRetryOn The exceptions to catch and retry on. Others will be propagated up the chain normally.
   * @param <V> The return value of the {@link Callable}.
   *
   * @return A {@link Callable} that works as described above.
   */
  public static <V> Retrier<V, NumTriesAndExceptionTracker> fixedTriesFixedDelay(Callable<V> c, int maxTries, long waitMillisBetweenTries, Collection<Class<? extends Exception>> exceptionsToRetryOn) {
    return new FixedTriesFixedDelayRetrier<>(c, maxTries, waitMillisBetweenTries, exceptionsToRetryOn);
  }
}