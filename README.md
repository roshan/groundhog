# Groundhog [![Build Status](https://travis-ci.org/roshan/groundhog.svg?branch=master)](https://travis-ci.org/roshan/groundhog)

Simple retry mechanism for Java Callables.

## Maven

```
<dependency>
  <groupId>com.arjie.groundhog</groupId>
  <artifactId>groundhog</artifactId>
  <version>0.7.0</version>
</dependency>
```

## Usage

Add the JAR to your classpath and then use with 

    RetryBuilders.fixedTriesFixedDelay(maxTries, waitMillisBetweenTries).buildAnnotatedFor(callable);

or

    RetryBuilders.basic()
        .withTryStrategy(new MaxTriesKnownExceptionTryStrategy<>(maxTries, Collections.<Class<? extends Exception>>singletonList(Exception.class))
        .withDelayStrategy(new FixedDelayStrategy<>(waitMillisBetweenTries))
	.buildAnnotatedFor(callable);
