/**
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazon.pay.api;

import com.amazon.pay.api.PayConfiguration;
import com.amazon.pay.api.ServiceConstants;
import com.amazon.pay.api.Util;
import com.amazon.pay.api.exceptions.AmazonPayClientException;
import com.amazon.pay.api.types.Environment;
import com.amazon.pay.api.types.Region;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class UtilTest {
    @Test
    public void urlEncode() throws Exception {
        String expectedUrl = Util.urlEncode("/live/v2/in-store/refund", true);
        Assert.assertEquals(expectedUrl, "/live/v2/in-store/refund");

        String expectedUrl1 = Util.urlEncode("/live/v2/in-store/refund", false);
        Assert.assertEquals(expectedUrl1, "%2Flive%2Fv2%2Fin-store%2Frefund");
    }

    @Test
    public void uriWithASpaceTest() throws Exception {
        String expectedUrl = Util.urlEncode("/ /foo", true);
        Assert.assertEquals(expectedUrl, "/%20/foo");
    }

    @Test
    public void uriWithRedundantSlashTest() throws Exception {
        String expectedUrl = Util.urlEncode("//", true);
        Assert.assertEquals(expectedUrl, "/");
    }

    @Test
    public void uriWithRedundantSlashesTest() throws Exception {
        String expectedUrl = Util.urlEncode("///foo//", true);
        Assert.assertEquals(expectedUrl, "/foo/");
    }

    @Test
    public void uriWithUnreservedCharactersTest() throws Exception {
        String expectedUrl = Util.urlEncode("/-._~0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", true);
        Assert.assertEquals(expectedUrl, "/-._~0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }

    @Test
    public void uriWithUTF8CharactersTest() throws Exception {
        String expectedUrl = Util.urlEncode("/\u1234", true);
        Assert.assertEquals(expectedUrl, "/%E1%88%B4");
    }

    @Test
    public void lowerCase() throws Exception {
        String str1 = "Signature";
        String str2 = "signature";
        String str3 = "SIGNATURE";

        Assert.assertEquals(Util.lowerCase(str1), "signature");
        Assert.assertEquals(Util.lowerCase(str2), "signature");
        Assert.assertEquals(Util.lowerCase(str3), "signature");
    }

    @Test
    public void getServiceURI() throws Exception {
        // Environment is Sandbox
        final PayConfiguration payConfiguration1 = new PayConfiguration()
                .setPublicKeyId("XXXXXXXXXXXXXXXXXXXXXXXX")
                .setRegion(Region.EU)
                .setEnvironment(Environment.SANDBOX);

        URI expectedURL = new URI("https://pay-api.amazon.eu/sandbox/v2/in-store/merchantScan");
        URI actualURL = Util.getServiceURI(payConfiguration1, ServiceConstants.INSTORE_MERCHANT_SCAN);
        Assert.assertEquals(expectedURL, actualURL);

        // Environment is Live
        final PayConfiguration payConfiguration2 = new PayConfiguration()
                .setPublicKeyId("XXXXXXXXXXXXXXXXXXXXXXXX")
                .setRegion(Region.EU)
                .setEnvironment(Environment.LIVE);
        expectedURL = new URI("https://pay-api.amazon.eu/live/v2/in-store/refund");
        actualURL = Util.getServiceURI(payConfiguration2, ServiceConstants.INSTORE_REFUND);
        Assert.assertEquals(expectedURL, actualURL);
        
        // Environment is Null (i.e Default value is live)
        final PayConfiguration payConfiguration3 = new PayConfiguration()
                .setPublicKeyId("XXXXXXXXXXXXXXXXXXXXXXXX")
                .setRegion(Region.EU);
        
        expectedURL = new URI("https://pay-api.amazon.eu/live/v2/in-store/refund");
        actualURL = Util.getServiceURI(payConfiguration3, ServiceConstants.INSTORE_REFUND);
        Assert.assertEquals(expectedURL, actualURL);

        // With Algorithm set in payConfiguration
        final PayConfiguration payConfigurationWithAlgorithm = new PayConfiguration()
                .setPublicKeyId("XXXXXXXXXXXXXXXXXXXXXXXX")
                .setRegion(Region.EU)
                .setAlgorithm("AMZN-PAY-RSASSA-PSS-V2");

        actualURL = Util.getServiceURI(payConfigurationWithAlgorithm, ServiceConstants.INSTORE_REFUND);
        Assert.assertEquals(expectedURL, actualURL);
    }
    
    @Test
    public void getServiceURIForEnvironmentSpecificKeys() throws Exception {
        final PayConfiguration payConfiguration1 = new PayConfiguration()
                .setPublicKeyId("LIVE-XXXXXXXXXXXXXXXXXXXXXXXX")
                .setRegion(Region.EU);

        URI expectedURL = new URI("https://pay-api.amazon.eu/v2/in-store/merchantScan");
        URI actualURL = Util.getServiceURI(payConfiguration1, ServiceConstants.INSTORE_MERCHANT_SCAN);
        Assert.assertEquals(expectedURL, actualURL);

        final PayConfiguration payConfiguration2 = new PayConfiguration()
                .setPublicKeyId("SANDBOX-XXXXXXXXXXXXXXXXXXXXXXXX")
                .setRegion(Region.EU);
        
        expectedURL = new URI("https://pay-api.amazon.eu/v2/in-store/refund");
        actualURL = Util.getServiceURI(payConfiguration2, ServiceConstants.INSTORE_REFUND);
        Assert.assertEquals(expectedURL, actualURL);

        // With Algorithm set in payConfiguration
        final PayConfiguration payConfigurationWithAlgorithm = new PayConfiguration()
                .setPublicKeyId("SANDBOX-XXXXXXXXXXXXXXXXXXXXXXXX")
                .setRegion(Region.EU)
                .setAlgorithm("AMZN-PAY-RSASSA-PSS-V2");

        actualURL = Util.getServiceURI(payConfigurationWithAlgorithm, ServiceConstants.INSTORE_REFUND);
        Assert.assertEquals(expectedURL, actualURL);
    }
    
    @Test
    public void updateHeader() throws Exception {
        PayConfiguration payConfiguration1 = new PayConfiguration()
                .setRegion(Region.EU)
                .setEnvironment(Environment.SANDBOX);

        Map<String, String> header = new HashMap<String, String>();
        Map<String, String> actualHeader = Util.updateHeader(header);
        Assert.assertTrue(!actualHeader.isEmpty());

        header = Util.updateHeader(null);
        Assert.assertNotNull(header);

    }

    @Test
    public void testGetHttpUriRequestForValidHttpMethod() throws UnsupportedEncodingException, AmazonPayClientException, URISyntaxException {
        final List<String> httpMethods = new ArrayList<String>() {
            {
                add("GET");
                add("POST");
                add("PUT");
                add("PATCH");
                add("HEAD");
                add("DELETE");
                add("OPTIONS");
                add("TRACE");
            }
        };
        for (String httpMethodName : httpMethods) {
            HttpUriRequest httpUriRequest = Util.getHttpUriRequest(new URI(StringUtils.EMPTY), httpMethodName,
                    StringUtils.EMPTY);
            Assert.assertEquals(httpMethodName, httpUriRequest.getMethod());
        }
    }

    @Test(expected = AmazonPayClientException.class)
    public void testGetHttpUriRequestForInvalidHttpMethod() throws UnsupportedEncodingException, AmazonPayClientException, URISyntaxException {
        Util.getHttpUriRequest(new URI(StringUtils.EMPTY), "Invalid", StringUtils.EMPTY);
    }
    
    @Test
    public void testGetCloseableHttpClientWithProxyMethod() {
        final String proxyhost = "host";
        final Integer proxyPort = 8080;
        final String proxyUser = "user";
        final char[] proxyPassword = new char[] {'p','a','s','s','w','o','r','d'};
        final ProxySettings proxySettings = new ProxySettings()
                .setProxyHost(proxyhost)
                .setProxyPort(proxyPort)
                .setProxyUser(proxyUser)
                .setProxyPassword(proxyPassword);
        final PayConfiguration payConfiguration = new PayConfiguration()
                .setProxySettings(proxySettings)
                .setClientConnections(ServiceConstants.MAX_CLIENT_CONNECTIONS);
        final CloseableHttpClient httpClient = Util.getCloseableHttpClientWithProxy(proxySettings, payConfiguration);
        // Assertions
        Assert.assertEquals(proxyhost, payConfiguration.getProxySettings().getProxyHost());
        Assert.assertEquals(proxyPort, payConfiguration.getProxySettings().getProxyPort());
        Assert.assertEquals(proxyUser, payConfiguration.getProxySettings().getProxyUser());
        Assert.assertEquals(proxyPassword, payConfiguration.getProxySettings().getProxyPassword());
        assertClientConnections(payConfiguration);
        Assert.assertNotNull(httpClient);
    }

    @Test
    public void testEnhanceResponseWithShippingAddressList() throws AmazonPayClientException, JSONException {
        JSONObject testShippingAddressListResponse = new JSONObject();
        testShippingAddressListResponse.put("shippingAddressList", new JSONArray().put("{\"addressId\":\"amzn1.address.ABC\",\"name\":\"DEF\",\"addressLine1\":\"GHI\",\"addressLine2\":\"JKL\",\"addressLine3\":null,\"city\":null,\"county\":null,\"district\":null,\"stateOrRegion\":\"MNO\",\"postalCode\":\"123-4567\",\"countryCode\":\"JP\",\"phoneNumber\":\"8910111213\"}"));

        final AmazonPayResponse testCheckoutSessionResponse = new AmazonPayResponse();
        testCheckoutSessionResponse.setResponse(testShippingAddressListResponse);

        final AmazonPayResponse actualCheckoutSessionResponseAfterEnhancing =  Util.enhanceResponseWithShippingAddressList(testCheckoutSessionResponse);
        
        final JSONObject expectedShippingAddress = new JSONObject();
        expectedShippingAddress.put("stateOrRegion", "MNO");
        expectedShippingAddress.put("phoneNumber", "8910111213");
        expectedShippingAddress.put("city", JSONObject.NULL);
        expectedShippingAddress.put("countryCode", "JP");
        expectedShippingAddress.put("district", JSONObject.NULL);
        expectedShippingAddress.put("postalCode", "123-4567");
        expectedShippingAddress.put("name", "DEF");
        expectedShippingAddress.put("county", JSONObject.NULL);
        expectedShippingAddress.put("addressLine1", "GHI");
        expectedShippingAddress.put("addressLine2", "JKL");
        expectedShippingAddress.put("addressLine3", JSONObject.NULL);
        expectedShippingAddress.put("addressId", "amzn1.address.ABC");
        
        JSONObject expectedShippingAddressListResponse = new JSONObject();
        expectedShippingAddressListResponse.put("shippingAddressList", new JSONArray().put(expectedShippingAddress));

        AmazonPayResponse expectedCheckoutSessionResponse = new AmazonPayResponse();
        expectedCheckoutSessionResponse.setResponse(expectedShippingAddressListResponse);
        expectedCheckoutSessionResponse.setRawResponse(expectedShippingAddressListResponse.toString());

        JSONObject expected = new JSONObject(expectedCheckoutSessionResponse.getRawResponse());
        JSONObject actual = new JSONObject(actualCheckoutSessionResponseAfterEnhancing.getRawResponse());
        Assert.assertTrue(expected.similar(actual));
    }

    @Test
    public void testGetHttpClientWithConnectionPool() {
        final PayConfiguration payConfiguration = new PayConfiguration()
                .setClientConnections(ServiceConstants.MAX_CLIENT_CONNECTIONS);
        final CloseableHttpClient httpClient = Util.getHttpClientWithConnectionPool(payConfiguration);
        // Assertions
        assertClientConnections(payConfiguration);
        Assert.assertNotNull(httpClient);
    }

    public void assertClientConnections(final PayConfiguration payConfiguration) {
        Assert.assertEquals(ServiceConstants.MAX_CLIENT_CONNECTIONS, payConfiguration.getClientConnections());
    }
}
