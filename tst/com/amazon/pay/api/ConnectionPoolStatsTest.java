package com.amazon.pay.api;

import com.amazon.pay.api.exceptions.AmazonPayClientException;
import com.amazon.pay.api.types.Environment;
import com.amazon.pay.api.types.Region;
import com.amazon.pay.api.ServiceConstants;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ConnectionPoolStatsTest {

    private PayConfiguration payConfiguration;
    private AmazonPayClient client;
    private ProxySettings proxySettings;
    private static final String proxyhost = "host";
    private static final Integer proxyPort = 8080;
    private static final String proxyUser = "user";
    private static final char[] proxyPassword = new char[] {'p','a','s','s','w','o','r','d'};
    RequestConfig requestConfig = Mockito.mock(RequestConfig.class);

    @Before
    public void setUp() throws Exception {

        proxySettings = new ProxySettings()
                .setProxyHost(proxyhost)
                .setProxyPort(proxyPort)
                .setProxyUser(proxyUser)
                .setProxyPassword(proxyPassword);
        payConfiguration = new PayConfiguration()
                .setPrivateKey(new String(Files.readAllBytes(
                        Paths.get("tst/com/amazon/pay/api/unit_test_private_key.txt"))).toCharArray())
                .setRegion(Region.NA)
                .setPublicKeyId("ABCDEF0000000000000")
                .setEnvironment(Environment.SANDBOX)
                .setClientConnections(ServiceConstants.MAX_CLIENT_CONNECTIONS)
                .setProxySettings(proxySettings)
                .setRequestConfig(requestConfig);

        client = new AmazonPayClient(payConfiguration);
    }

    @Test
    public void testDefaultConnectionPool() {
        final ConnectionPoolStats stats = new ConnectionPoolStats(0, 0, 0 ,0);
        Assert.assertEquals(0, stats.getMaxConnections());
        Assert.assertEquals(0, stats.getAvailableConnections());
        Assert.assertEquals(0, stats.getLeasedConnections());
        Assert.assertEquals(0, stats.getPendingConnections());
    }

    @Test
    public void testCustomConnectionPool() {
        final ConnectionPoolStats stats = new ConnectionPoolStats(ServiceConstants.MAX_CLIENT_CONNECTIONS, 1, 0, 0);
        Assert.assertEquals(ServiceConstants.MAX_CLIENT_CONNECTIONS, stats.getMaxConnections());
        Assert.assertEquals(1, stats.getAvailableConnections());
        Assert.assertEquals(0, stats.getLeasedConnections());
        Assert.assertEquals(0, stats.getPendingConnections());
    }

    @Test
    public void testGetPoolStats() throws AmazonPayClientException, Exception {
        final ConnectionPoolStats actualPoolStats = client.getPoolStats();
        final PoolingHttpClientConnectionManager connectionManager = client.connectionManager;
        final PoolStats expectedPoolStats = connectionManager.getTotalStats();
        // Assertions
        Assert.assertEquals(actualPoolStats.getMaxConnections(), expectedPoolStats.getMax());
        Assert.assertEquals(actualPoolStats.getAvailableConnections(), expectedPoolStats.getAvailable());
        Assert.assertEquals(actualPoolStats.getLeasedConnections(), expectedPoolStats.getLeased());
        Assert.assertEquals(actualPoolStats.getPendingConnections(), expectedPoolStats.getPending());
    }

    @Test
    public void testGetHttpClientWithConnectionPool() {
        final CloseableHttpClient httpClient = client.getClosableHttpClientWithConnectionPool();
        Assert.assertNotNull(httpClient);
    }

    @Test
    public void testGetHttpClientWithPoolAndProxy() {
        final CloseableHttpClient httpClient = client.getClosableHttpClientWithPoolAndProxy();
        Assert.assertNotNull(httpClient);
    }

    @Test
    public void testToString() {
        final ConnectionPoolStats stats = new ConnectionPoolStats(ServiceConstants.MAX_CLIENT_CONNECTIONS, 2, 1, 1);
        String expectedOutput = "ConnectionPoolStats{Total=" + ServiceConstants.MAX_CLIENT_CONNECTIONS + ", Available=2, Pending=1, Leased=1}";
        String actualOutput = stats.toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetCloseableHttpClientWithProxyRequestConfigIsSet() {

        Mockito.when(requestConfig.getConnectTimeoutMillis()).thenReturn(1);
        Mockito.when(requestConfig.getConnectionTimeoutMillis()).thenReturn(2);
        Mockito.when(requestConfig.getSocketTimeoutMillis()).thenReturn(3);

        client.getClosableHttpClientWithPoolAndProxy();

        Mockito.verify(requestConfig, Mockito.times(1)).getConnectTimeoutMillis();
        Mockito.verify(requestConfig, Mockito.times(1)).getConnectTimeoutMillis();
        Mockito.verify(requestConfig, Mockito.times(1)).getSocketTimeoutMillis();
    }

    @Test
    public void testGetHttpClientWithConnectionPoolRequestConfigIsSet() {
        Mockito.when(requestConfig.getConnectTimeoutMillis()).thenReturn(1);
        Mockito.when(requestConfig.getConnectionTimeoutMillis()).thenReturn(2);
        Mockito.when(requestConfig.getSocketTimeoutMillis()).thenReturn(3);

        client.getClosableHttpClientWithConnectionPool();

        Mockito.verify(requestConfig, Mockito.times(1)).getConnectTimeoutMillis();
        Mockito.verify(requestConfig, Mockito.times(1)).getConnectTimeoutMillis();
        Mockito.verify(requestConfig, Mockito.times(1)).getSocketTimeoutMillis();
    }
}
