package com.amazon.pay.api;

import org.junit.Assert;
import org.junit.Test;

public class DefaultRetryStrategyTest {
    @Test
    public void testDefaults() {
        RetryStrategy retryStrategy = new DefaultRetryStrategy();
        Assert.assertEquals(3, retryStrategy.getMaxRetries());
        Assert.assertEquals(2000, retryStrategy.getWaitTime(0, 100));
        Assert.assertEquals(2000, retryStrategy.getWaitTime(0, 429));
        Assert.assertEquals(4000, retryStrategy.getWaitTime(1, 100));
        Assert.assertEquals(4000, retryStrategy.getWaitTime(1, 429));
        Assert.assertEquals(8000, retryStrategy.getWaitTime(2, 100));
        Assert.assertEquals(8000, retryStrategy.getWaitTime(2, 429));
    }
}
