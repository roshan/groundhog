# Groundhog [![Build Status](https://travis-ci.org/roshan/groundhog.svg?branch=master)](https://travis-ci.org/roshan/groundhog)

Simple retry mechanism for Java Callables.

## Maven

```
<dependency>
  <groupId>com.arjie.groundhog</groupId>
  <artifactId>groundhog</artifactId>
  <version>0.9.0</version>
</dependency>
```

## Usage

Add the JAR to your classpath and then use with 

```
RetryBuilders.basic()
    .withTryStrategy(new MaxTries.Builder<>()
      .setMaxTries(maxTries)
      .addExceptionToRetryOn(IOException.class)
      .build())
    .withDelayStrategy(new FixedDelay<>(waitMillisBetweenTries));
```

or with the two convenience constructors:

```
RetryBuilders.fixedTriesFixedDelay(maxTries, waitMillisBetweenTries).build(callable)
```

```
RetryBuilders.fixedTriesExponentialBackoff(maxTries, delayFactor, initialDelayInMillis).build(callable);
```

Use `RetryBuilder#annotate` instead of `RetryBuilder#build` to get the final state as well as the finally returned value.
