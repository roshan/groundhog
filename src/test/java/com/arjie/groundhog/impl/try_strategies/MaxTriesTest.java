package com.arjie.groundhog.impl.try_strategies;

import java.util.ArrayList;

import com.arjie.groundhog.impl.NumTries;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MaxTriesTest {
  @Test
  public void shouldTry() {
    int maxAttempts = 3;
    MaxTries<NumTries> strategy = new MaxTries.Builder().setMaxTries(maxAttempts).retryAll();
    NumTries state = new NumTries();
    for(int i = 0; i < maxAttempts - 1; i++) {
      state.prepareForAttempt();
      state.updateForExceptionThrown(new RuntimeException());
      assertTrue(strategy.shouldTry(state));
    }
    state.prepareForAttempt();
    state.updateForExceptionThrown(new RuntimeException());
    assertFalse(strategy.shouldTry(state));
  }
}
