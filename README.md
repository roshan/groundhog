# Groundhog [![Build Status](https://travis-ci.org/roshan/groundhog.svg?branch=master)](https://travis-ci.org/roshan/groundhog)

Simple retry mechanism for Java Callables.

## Maven

```
<dependency>
  <groupId>com.arjie.groundhog</groupId>
  <artifactId>groundhog</artifactId>
  <version>0.4.0</version>
</dependency>
```

## Usage

Add the JAR to your classpath and then use with 

    Retriers.fixedTriesFixedDelay(yourCallable, someNumTries, waitTimeInMilliseconds)
