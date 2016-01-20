# Groundhog [![Build Status](https://travis-ci.org/roshan/groundhog.svg?branch=master)](https://travis-ci.org/roshan/groundhog)

Simple retry mechanism for Java Callables.

## Usage

Add the JAR to your classpath and then use with 

    Retriers.fixedTriesFixedDelayRetrier(yourCallable, someNumTries, waitTimeInMilliseconds)
