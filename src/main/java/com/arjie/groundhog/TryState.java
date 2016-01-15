package com.arjie.groundhog;

/**
 * State that is tracked and used to determine whether to retry or not.
 */
public interface TryState {
  /**
   * Guaranteed to be called before an attempt is made.
   */
  void prepareForAttempt();

  /**
   * Guaranteed to be called when an exception is thrown with the thrown exception.
   * @param exception The exception that was thrown.
   * @param <E> The kind of exception.
   */
  <E extends Exception> void updateForExceptionThrown(E exception);

  /**
   * A means by which a fresh state of type {@link T} can be constructed.
   */
  interface Factory<T extends TryState> {
    /**
     * @return A fresh state of type {@link T}.
     */
    T constructInitialState();
  }
}
