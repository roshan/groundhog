# Groundhog

Simple retry mechanism for Java Callables.

# Usage

Add the JAR to your classpath and then use with 

    Retriers.fixedTriesFixedDelayRetrier(yourCallable, someNumTries, waitTimeInMilliseconds)
