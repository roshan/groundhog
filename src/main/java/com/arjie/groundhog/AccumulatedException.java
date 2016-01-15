package com.arjie.groundhog;

import java.util.List;

/**
 * An exception that carries all the thrown exceptions within, and the {@link TryState} when it was thrown.
 */
public class AccumulatedException extends Exception {
  private final List<Exception> exceptions;
  private final TryState terminalFailureState;

  public AccumulatedException(String message, List<Exception> exceptions, TryState terminalFailureState) {
    super(message);
    this.exceptions = exceptions;
    this.terminalFailureState = terminalFailureState;
  }

  public List<Exception> getExceptions() {
    return exceptions;
  }

  /**
   * @return The {@link TryState} when the exception was thrown. When this exception is thrown by a
   * {@link Retrier}, this can be safely cast to the type of the {@link TryState} used by the
   * {@link Retrier}.
   */
  public TryState getTerminalFailureState() {
    return terminalFailureState;
  }
}
