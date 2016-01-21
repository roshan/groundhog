package com.arjie.groundhog;

public class RetryResult<V, S> {
  private final S terminalState;
  private final V val;

  public RetryResult(S terminalState, V val) {
    this.terminalState = terminalState;
    this.val = val;
  }

  public V getReturnValue() {
    return val;
  }

  public S getTerminalState() {
    return terminalState;
  }
}
