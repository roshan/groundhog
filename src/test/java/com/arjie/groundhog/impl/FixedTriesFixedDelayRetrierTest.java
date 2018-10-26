package com.arjie.groundhog.impl;

import com.arjie.groundhog.AccumulatedException;
import com.arjie.groundhog.RetryResult;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked") // only warnings are on Mockito
public class FixedTriesFixedDelayRetrierTest {

  @Test
  public void testDoesNotRunForZeroTries() throws AccumulatedException {
    Callable<Void> c = Mockito.mock(Callable.class);
    FixedTriesFixedDelayRetrier retrier = new FixedTriesFixedDelayRetrier<>(c, 0, 0, Collections.<Class<? extends Exception>>singleton(Exception.class));
    try {
      retrier.call();
      fail("Should not have successfully called.");
    } catch (AccumulatedException ignored) {
    }
    Mockito.verifyZeroInteractions(c);
  }

  @Test
  public void testThrowsImmediatelyOnFirstTry() throws Exception {
    Callable<Void> c = Mockito.mock(Callable.class);
    when(c.call()).thenThrow(Exception.class);
    FixedTriesFixedDelayRetrier retrier = new FixedTriesFixedDelayRetrier<>(c, 1, 0, Collections.<Class<? extends Exception>>singleton(Exception.class));
    try {
      retrier.call();
      fail("Should not have successfully called.");
    } catch (AccumulatedException ignored) {
    }
  }

  @Test
  public void testRetriesKTimesOnFailure() throws Exception {
    long ret = 10l;

    Callable<Long> c = Mockito.mock(Callable.class);
    when(c.call()).thenThrow(Exception.class).thenThrow(Exception.class).thenThrow(Exception.class).thenReturn(ret);

    FixedTriesFixedDelayRetrier<Long> retrier = new FixedTriesFixedDelayRetrier<>(c, 4, 0, Collections.<Class<? extends Exception>>singleton(Exception.class));

    RetryResult<Long, NumTries> result = retrier.call();
    NumTries terminalState = result.getTerminalState();

    assertEquals(4, terminalState.getNumTries()); // 3 throws + 1 return
    assertEquals(Exception.class, terminalState.getLastException().getClass());
    assertEquals(ret, result.getReturnValue().longValue());
  }

  @Test
  public void testStopsTryingAfterSuccess() throws Exception {
    long ret = 10l;
    Callable<Long> c = Mockito.mock(Callable.class);
    when(c.call()).thenReturn(ret);

    FixedTriesFixedDelayRetrier<Long> retrier = new FixedTriesFixedDelayRetrier<>(c, 10, 0, Collections.<Class<? extends Exception>>singleton(Exception.class));

    RetryResult<Long, NumTries> result = retrier.call();

    assertEquals(1, result.getTerminalState().getNumTries());
    assertEquals(ret, result.getReturnValue().longValue());
  }

  @Test
  public void testCatchesExceptionSubclass() throws Exception {
    long ret = 10l;
    Callable<Long> c = Mockito.mock(Callable.class);
    when(c.call()).thenThrow(SubException.class).thenReturn(ret);

    FixedTriesFixedDelayRetrier<Long> retrier = new FixedTriesFixedDelayRetrier<>(c, 2, 0, Collections.<Class<? extends Exception>>singleton(Exception.class));

    RetryResult<Long, NumTries> result = retrier.call();

    assertEquals(2, result.getTerminalState().getNumTries());
    assertEquals(ret, result.getReturnValue().longValue());
    assertEquals(SubException.class, result.getTerminalState().getLastException().getClass());
  }

  @Test
  public void testDoesNotCatchExceptionSuperclass() throws Exception {
    long ret = 10l;
    Callable<Long> c = Mockito.mock(Callable.class);
    when(c.call()).thenThrow(Exception.class).thenReturn(ret);

    FixedTriesFixedDelayRetrier<Long> retrier = new FixedTriesFixedDelayRetrier<>(c, 3, 0, Collections.<Class<? extends Exception>>singleton(SubException.class));

    try {
      retrier.call();
      fail();
    } catch (AccumulatedException ignored) {
      assertEquals(1, ignored.getExceptions().size()); // only one try despite being able to retry up to 3 times
    }
  }

  @Test
  public void testCanCatchRuntimeException() throws Exception {
    long ret = 10l;
    Callable<Long> c = Mockito.mock(Callable.class);
    when(c.call()).thenThrow(SubRuntimeException.class).thenReturn(ret);

    FixedTriesFixedDelayRetrier<Long> retrier = new FixedTriesFixedDelayRetrier<>(c, 2, 0, Collections.<Class<? extends Exception>>singleton(RuntimeException.class));

    RetryResult<Long, NumTries> result = retrier.call();

    assertEquals(2, result.getTerminalState().getNumTries());
    assertEquals(ret, result.getReturnValue().longValue());
    assertEquals(SubRuntimeException.class, result.getTerminalState().getLastException().getClass());
  }

  private static class SubException extends Exception{}
  private static class SubRuntimeException extends RuntimeException{}

}
