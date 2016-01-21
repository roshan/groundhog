package com.arjie.groundhog;

public interface DelayStrategy<S extends TryState> {
  /**
   * @param state The state to make a decision on.
   * @return the number of milliseconds to wait before seeing if we should make another attempt and then doing so.
   */
  long getMillisToDelayRetry(S state);
}
