package com.arjie.groundhog;

/**
 * The tool that determines when a try should be made and how long we should delay before attempting another try.
 *
 * @param <S> The kind of state used to make decisions.
 */
public interface TryStrategy<S extends TryState> {
  /**
   * @param state The state to make a decision on.
   * @return true if we should make an attempt, false otherwise.
   */
  boolean shouldTry(S state);
}
