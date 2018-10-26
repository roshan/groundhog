package com.arjie.groundhog.impl;

import com.arjie.groundhog.DelayStrategy;
import com.arjie.groundhog.impl.delay_strategies.ExponentialDelay;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExponentialDelayTest {

  @Test
  public void testInitialDelay() {
    NumTries mockState = mock(NumTries.class);
    when(mockState.getNumTries()).thenReturn(1l);

    int coeff = 5;
    ExponentialDelay<NumTries> strategy = new ExponentialDelay<>(100, coeff);
    assertEquals(coeff, strategy.getMillisToDelayRetry(mockState));
  }

  @Test
  public void testDelayExponentiallyIncreases() {
    NumTries initialMockState = mock(NumTries.class);
    when(initialMockState.getNumTries()).thenReturn(1l);

    NumTries secondMockState = mock(NumTries.class);
    when(secondMockState.getNumTries()).thenReturn(2l);

    NumTries thirdMockState = mock(NumTries.class);
    when(thirdMockState.getNumTries()).thenReturn(3l);

    int base = 5;
    DelayStrategy<NumTries> strategy = new ExponentialDelay<>(base, 10);

    long initial = strategy.getMillisToDelayRetry(initialMockState);
    long second = strategy.getMillisToDelayRetry(secondMockState);
    long third = strategy.getMillisToDelayRetry(thirdMockState);

    assertEquals(second/initial, third/second);
    assertEquals(base, second/initial);

  }

}
