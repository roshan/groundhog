package com.arjie.groundhog.impl;

import com.arjie.groundhog.TryState;

/**
 * Tracks the number of tries made and the most recent exception thrown.
 */
public class NumTries implements TryState {

  private long numTries;
  private Exception lastException = null;

  public NumTries() {
    numTries = 0;
  }

  @Override
  public void prepareForAttempt() {
    numTries++;
  }

  @Override
  public <E extends Exception> void updateForExceptionThrown(E exception) {
    lastException = exception;
  }

  public long getNumTries() {
    return numTries;
  }

  public Exception getLastException() {
    return lastException;
  }

  public static class Factory implements TryState.Factory<NumTries> {
    @Override
    public NumTries constructInitialState() {
      return new NumTries();
    }
  }
}
