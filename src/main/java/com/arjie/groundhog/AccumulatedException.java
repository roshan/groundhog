package com.arjie.groundhog;

import java.util.Collections;
import java.util.List;

/**
 * An exception that carries all the thrown exceptions within, and the {@link TryState} when it was thrown.
 */
public class AccumulatedException extends Exception {
  private final List<Exception> exceptions;
  private final TryState terminalFailureState;

  public AccumulatedException(String message, TryState terminalFailureState) {
    super(message);
    this.exceptions = Collections.emptyList();
    this.terminalFailureState = terminalFailureState;
  }

  public AccumulatedException(String message, List<Exception> exceptions, Exception lastException, TryState terminalFailureState) {
    super(message, lastException);

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

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder(String.format("%d exceptions collected.", exceptions.size()));

    for (Exception exception : exceptions) {
      s.append(String.format("\n\t%s", exception.toString()));
    }

    s.append("\n");
    s.append(super.toString());

    return s.toString();
  }
}
