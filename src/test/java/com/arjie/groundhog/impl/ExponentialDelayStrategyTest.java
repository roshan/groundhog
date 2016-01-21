package com.arjie.groundhog.impl;

import com.arjie.groundhog.DelayStrategy;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExponentialDelayStrategyTest {

  @Test
  public void testInitialDelay() {
    NumTriesAndExceptionTracker mockState = mock(NumTriesAndExceptionTracker.class);
    when(mockState.getNumTries()).thenReturn(1l);

    int coeff = 5;
    ExponentialDelayStrategy<NumTriesAndExceptionTracker> strategy = new ExponentialDelayStrategy<>(100, coeff);
    assertEquals(coeff, strategy.getMillisToDelayRetry(mockState));
  }

  @Test
  public void testDelayExponentiallyIncreases() {
    NumTriesAndExceptionTracker initialMockState = mock(NumTriesAndExceptionTracker.class);
    when(initialMockState.getNumTries()).thenReturn(1l);

    NumTriesAndExceptionTracker secondMockState = mock(NumTriesAndExceptionTracker.class);
    when(secondMockState.getNumTries()).thenReturn(2l);

    NumTriesAndExceptionTracker thirdMockState = mock(NumTriesAndExceptionTracker.class);
    when(thirdMockState.getNumTries()).thenReturn(3l);

    int base = 5;
    DelayStrategy<NumTriesAndExceptionTracker> strategy = new ExponentialDelayStrategy<>(base, 10);

    long initial = strategy.getMillisToDelayRetry(initialMockState);
    long second = strategy.getMillisToDelayRetry(secondMockState);
    long third = strategy.getMillisToDelayRetry(thirdMockState);

    assertEquals(second/initial, third/second);
    assertEquals(base, second/initial);

  }

}
