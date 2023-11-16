package com.amazon.pay.api;

import org.junit.Assert;
import org.junit.Test;

public class PayConfigurationTest {
    @Test
    public void testDefaultRetryConfiguration() {
        PayConfiguration payConfiguration = new PayConfiguration();
        Assert.assertEquals(3, payConfiguration.getMaxRetries());
        Assert.assertTrue(payConfiguration.getRetryStrategy() instanceof DefaultRetryStrategy);
    }

    @Test
    public void testUpdatingMaxRetries() {
        PayConfiguration payConfiguration = new PayConfiguration();
        payConfiguration.setMaxRetries(1);
        Assert.assertEquals(1, payConfiguration.getMaxRetries());
    }

    @Test
    public void testUpdatingRetryStrategy() {
        PayConfiguration payConfiguration = new PayConfiguration();
        ExponentialRetryStrategy retryStrategy = new ExponentialRetryStrategy(5, 500);
        PayConfiguration configuration = payConfiguration.setRetryStrategy(retryStrategy);
        Assert.assertEquals(payConfiguration, configuration);
        Assert.assertFalse(payConfiguration.getRetryStrategy() instanceof DefaultRetryStrategy);
        Assert.assertTrue(payConfiguration.getRetryStrategy() instanceof ExponentialRetryStrategy);
        Assert.assertEquals(5, payConfiguration.getMaxRetries());

    }

    @Test
    public void testDefaultRequestConfigIsNull() {
        PayConfiguration payConfiguration = new PayConfiguration();
        Assert.assertNull(payConfiguration.getRequestConfig());
    }

    @Test
    public void testRequestConfig() {
        PayConfiguration payConfiguration = new PayConfiguration();
        Assert.assertNull(payConfiguration.getRequestConfig());
        RequestConfig requestConfig = new RequestConfig(10, 50, 150);

        PayConfiguration configuration = payConfiguration.setRequestConfig(requestConfig);
        Assert.assertEquals(10, (long) payConfiguration.getRequestConfig().getConnectionTimeoutMillis());
        Assert.assertEquals(50, (long) payConfiguration.getRequestConfig().getConnectTimeoutMillis());
        Assert.assertEquals(150, (long) payConfiguration.getRequestConfig().getSocketTimeoutMillis());
    }

    @Test
    public void testDefaultClientConnections() {
        PayConfiguration payConfiguration = new PayConfiguration();
        Assert.assertEquals(ServiceConstants.MAX_CLIENT_CONNECTIONS, payConfiguration.getClientConnections());
    }

    @Test
    public void testCustomClientConnections() {
        PayConfiguration payConfiguration = new PayConfiguration();
        payConfiguration.setClientConnections(10);
        Assert.assertEquals(10, payConfiguration.getClientConnections());
    }

    @Test
    public void testCustomConnectionsPoolWithZeroConnections() {
        PayConfiguration payConfiguration = new PayConfiguration();
        payConfiguration.setClientConnections(0);
        Assert.assertEquals(ServiceConstants.MAX_CLIENT_CONNECTIONS, payConfiguration.getClientConnections());
    }
}
