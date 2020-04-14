/**
 * Copyright 2018-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

import com.amazon.pay.api.types.Environment;
import com.amazon.pay.api.types.Region;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
        PayConfiguration payConfiguration1 = new PayConfiguration()
                .setRegion(Region.EU)
                .setEnvironment(Environment.SANDBOX);

        URI expectedURL = new URI("https://pay-api.amazon.eu/sandbox/v2/in-store/merchantScan");
        URI actualURL = Util.getServiceURI(payConfiguration1, ServiceConstants.INSTORE_MERCHANT_SCAN);
        Assert.assertEquals(expectedURL, actualURL);

        PayConfiguration payConfiguration2 = new PayConfiguration()
                .setRegion(Region.EU);
        expectedURL = new URI("https://pay-api.amazon.eu/live/v2/in-store/refund");
        actualURL = Util.getServiceURI(payConfiguration2, ServiceConstants.INSTORE_REFUND);
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
}