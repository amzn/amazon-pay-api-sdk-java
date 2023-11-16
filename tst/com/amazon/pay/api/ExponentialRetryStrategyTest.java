package com.amazon.pay.api;

import org.junit.Assert;
import org.junit.Test;

public class ExponentialRetryStrategyTest {
    @Test
    public void testBasicConstructor() {
        ExponentialRetryStrategy strategy = new ExponentialRetryStrategy(7, 1234);
        Assert.assertEquals(1234, strategy.getWaitTime(0, 100));
        Assert.assertEquals(1234, strategy.getWaitTime(0, 429));
        Assert.assertEquals(7, strategy.getMaxRetries());
    }

    @Test
    public void testIntermediateConstructor() {
        ExponentialRetryStrategy strategy = new ExponentialRetryStrategy(7, 1234, 5678);
        Assert.assertEquals(1234, strategy.getWaitTime(0, 100));
        Assert.assertEquals(1234*2, strategy.getWaitTime(1, 100));
        Assert.assertEquals(1234*4, strategy.getWaitTime(2, 100));
        Assert.assertEquals(1234*8, strategy.getWaitTime(3, 100));
        Assert.assertEquals(5678, strategy.getWaitTime(0, 429));
        Assert.assertEquals(5678*2, strategy.getWaitTime(1, 429));
        Assert.assertEquals(5678*4, strategy.getWaitTime(2, 429));
        Assert.assertEquals(5678*8, strategy.getWaitTime(3, 429));
        Assert.assertEquals(7, strategy.getMaxRetries());
    }

    @Test
    public void testComplexConstructor() {
        ExponentialRetryStrategy strategy = new ExponentialRetryStrategy(7, 1234, 5678, 2*1234, 4*5678);
        Assert.assertEquals(1234, strategy.getWaitTime(0, 100));
        Assert.assertEquals(1234*2, strategy.getWaitTime(1, 100));
        Assert.assertEquals(1234*2, strategy.getWaitTime(2, 100));
        Assert.assertEquals(1234*2, strategy.getWaitTime(3, 100));
        Assert.assertEquals(5678, strategy.getWaitTime(0, 429));
        Assert.assertEquals(5678*2, strategy.getWaitTime(1, 429));
        Assert.assertEquals(5678*4, strategy.getWaitTime(2, 429));
        Assert.assertEquals(5678*4, strategy.getWaitTime(3, 429));
        Assert.assertEquals(7, strategy.getMaxRetries());
    }

    @Test
    public void testSetMaxRetries() {
        ExponentialRetryStrategy strategy = new ExponentialRetryStrategy(7, 1234);
        Assert.assertEquals(7, strategy.getMaxRetries());
        strategy.setMaxRetries(5);
        Assert.assertEquals(5, strategy.getMaxRetries());
    }

    @Test
    public void testGetExponentialWaitTime() {
        ExponentialRetryStrategy strategy = new ExponentialRetryStrategy(1, 1);
        Assert.assertEquals(1000L, strategy.getExponentialWaitTime(0, 1000L));
        Assert.assertEquals(2000L, strategy.getExponentialWaitTime(1, 1000L));
        Assert.assertEquals(4000L, strategy.getExponentialWaitTime(2, 1000L));

        Assert.assertEquals(2000L, strategy.getExponentialWaitTime(0, 2000L));
        Assert.assertEquals(4000L, strategy.getExponentialWaitTime(1, 2000L));
        Assert.assertEquals(8000L, strategy.getExponentialWaitTime(2, 2000L));
    }

}
