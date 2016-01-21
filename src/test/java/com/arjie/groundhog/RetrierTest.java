package com.arjie.groundhog;

import com.arjie.groundhog.impl.NumTriesAndExceptionTracker;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.Callable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked") // Mockito with generics
public class RetrierTest {

  @Test
  public void testImmediateSuccessCausesNoRetryEvenIfStrategyWouldAllow() throws Exception {
    long retVal = 0l;

    TryStrategy mockTryStrategy = mock(TryStrategy.class);
    when(mockTryStrategy.shouldTry(Mockito.<TryState>any())).thenReturn(true);

    Callable mockCallable = mock(Callable.class);
    when(mockCallable.call()).thenReturn(retVal);

    Retrier<Long, NumTriesAndExceptionTracker> retrier = new Retrier<>(mockCallable, mockTryStrategy, mock(DelayStrategy.class), new NumTriesAndExceptionTracker.Factory());

    RetryResult<Long, NumTriesAndExceptionTracker> result = retrier.call();
    assertEquals(result.getReturnValue().longValue(), retVal);

    Mockito.verify(mockCallable, Mockito.times(1)).call();
    Mockito.verify(mockTryStrategy, Mockito.times(1)).shouldTry(Mockito.<TryState>any());
  }

  @Test
  public void testNoAttemptIfRequirementToTryNotMet() throws Exception {
    TryStrategy mockTryStrategy = mock(TryStrategy.class);
    when(mockTryStrategy.shouldTry(Mockito.<TryState>any())).thenReturn(false);

    Callable mockCallable = mock(Callable.class);
    when(mockCallable.call()).thenReturn(0);

    Retrier<Long, TryState> retrier = new Retrier<>(mockCallable, mockTryStrategy, mock(DelayStrategy.class), mock(TryState.Factory.class));

    try {
      retrier.call();
      fail("Should not have returned successfully.");
    } catch (AccumulatedException e) {
      assertEquals(0, e.getExceptions().size());
    }

    Mockito.verify(mockCallable, Mockito.never()).call();
    Mockito.verify(mockTryStrategy, Mockito.times(1)).shouldTry(Mockito.<TryState>any());
  }

  @Test
  public void testReattemptsIfAndOnlyIfStrategyPermits() throws Exception {
    Callable mockCallable = mock(Callable.class);
    when(mockCallable.call()).thenThrow(Exception.class);

    TryStrategy mockTryStrategy = mock(TryStrategy.class);
    when(mockTryStrategy.shouldTry(Mockito.<TryState>any())).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);

    DelayStrategy mockDelayStrategy = mock(DelayStrategy.class);

    TryState mockState = mock(TryState.class);
    TryState.Factory mockStateFactory = mock(TryState.Factory.class);
    when(mockStateFactory.constructInitialState()).thenReturn(mockState);

    Retrier<Long, TryState> retrier = new Retrier<>(mockCallable, mockTryStrategy, mockDelayStrategy, mockStateFactory);

    try {
      retrier.call();
      fail("Should not have returned successfully.");
    } catch (AccumulatedException e) {
      assertTrue(1 < e.getExceptions().size());
      Mockito.verify(mockCallable, Mockito.times(e.getExceptions().size())).call();
      Mockito.verify(mockDelayStrategy, Mockito.times(e.getExceptions().size())).getMillisToDelayRetry(Mockito.<TryState>any());
    }

  }

  @Test
  public void testSucceedsIfReattemptSucceeds() throws Exception {
    long expectedRet = 1l;

    Callable mockCallable = mock(Callable.class);
    when(mockCallable.call()).thenThrow(Exception.class).thenReturn(expectedRet);

    TryStrategy mockTryStrategy = mock(TryStrategy.class);
    when(mockTryStrategy.shouldTry(Mockito.<TryState>any())).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);

    DelayStrategy mockDelayStrategy = mock(DelayStrategy.class);

    TryState mockState = mock(TryState.class);
    TryState.Factory mockStateFactory = mock(TryState.Factory.class);
    when(mockStateFactory.constructInitialState()).thenReturn(mockState);

    Retrier<Long, TryState> retrier = new Retrier<>(mockCallable, mockTryStrategy, mockDelayStrategy, mockStateFactory);

    RetryResult<Long, TryState> call = retrier.call();
    assertEquals(expectedRet, call.getReturnValue().longValue());
  }


}
