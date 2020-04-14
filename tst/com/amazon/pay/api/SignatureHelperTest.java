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

import com.amazon.pay.api.types.Region;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.support.membermodification.MemberMatcher.method;


@RunWith(PowerMockRunner.class)
@PrepareForTest(SignatureHelper.class)
public class SignatureHelperTest {
    private SignatureHelper spy;
    private PayConfiguration payConfiguration = new PayConfiguration();
    URI uri;
    private Map<String, List<String>> preSignedHeaders;
    private Map<String, String> header;

    @Before
    public void setUp() throws Exception {
        payConfiguration.setRegion(Region.EU).setPublicKeyId("");
        spy = spy(new SignatureHelper(payConfiguration));
        uri = new URI("https://pay-api.amazon.eu/live/v2/in-store/refund");
        header = new HashMap<String, String>();
        preSignedHeaders = mockedPreSignedHeaders(uri, header);
    }

    @Test
    public void createStringToSign() throws Exception {
        String canonicalRequest = "POST\n/live/v2/in-store/refund\naccept:application/json\ncontent-type:application/json\naccept;content-type\n95b0d65e9efb9f0b9e8c2f3b7744628c765477";
        PowerMockito.when(spy, method(SignatureHelper.class, "hashThenHexEncode")).withArguments(canonicalRequest).thenReturn("95b0d65e9efb9f0b9e8c2f3b77");

        String stringToSign = spy.createStringToSign(canonicalRequest);
        String expectedString = "AMZN-PAY-RSASSA-PSS" + "\n" + "95b0d65e9efb9f0b9e8c2f3b77";

        Assert.assertEquals(stringToSign, expectedString);
    }

    @Test
    public void createPreSignedHeaders() throws Exception {
        Map<String, List<String>> actualHeaders = spy.createPreSignedHeaders(uri, header);
        Assert.assertEquals(preSignedHeaders, actualHeaders);

    }

    @Test
    public void getSignedHeadersString() throws Exception {
        String expectedHeadersString = "accept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region";

        String actualHeadersString = spy.getSignedHeadersString(preSignedHeaders);
        Assert.assertEquals(expectedHeadersString, actualHeadersString);
    }

    @Test
    public void getCanonicalizedHeaderString() throws Exception {
        String expectedCanonicalHeaderString = "accept:application/json\ncontent-type:application/json\nx-amz-pay-date:" + preSignedHeaders.get("x-amz-pay-date").get(0) + "\nx-amz-pay-host:pay-api.amazon.eu\nx-amz-pay-region:EU\n";
        String actualCanonicalHeaderString = spy.getCanonicalizedHeaderString(preSignedHeaders);

        Assert.assertEquals(expectedCanonicalHeaderString, actualCanonicalHeaderString);
    }

    @Test
    public void getCanonicalizedQueryString() throws Exception {
        Map<String, List<String>> parametersMap = new HashMap<>();
        List<String> amount = new ArrayList<>();
        amount.add("100.50");
        parametersMap.put("amount", amount);
        List<String> info = new ArrayList<>();
        info.add("Information about order");
        parametersMap.put("customInformation", info);
        String expectedCanonicalQueryString = "amount=100.50&customInformation=Information%20about%20order";
        String actualCanonicalQueryString = spy.getCanonicalizedQueryString(parametersMap);

        Assert.assertEquals(expectedCanonicalQueryString, actualCanonicalQueryString);
    }

    private Map<String, List<String>> mockedPreSignedHeaders(URI uri, Map<String, String> header) throws URISyntaxException {
        Map<String, List<String>> headers = new HashMap<>();

        List<String> acceptHeaderValue = new ArrayList<>();
        acceptHeaderValue.add("application/json");
        headers.put("accept", acceptHeaderValue);

        List<String> contentHeaderValue = new ArrayList<>();
        contentHeaderValue.add("application/json");
        headers.put("content-type", contentHeaderValue);

        List<String> regionHeaderValue = new ArrayList<>();
        regionHeaderValue.add(payConfiguration.getRegion().toString());
        headers.put("x-amz-pay-region", regionHeaderValue);

        List<String> dateHeaderValue = new ArrayList<>();
        dateHeaderValue.add(Util.getFormattedTimestamp());
        headers.put("x-amz-pay-date", dateHeaderValue);

        List<String> hostHeaderValue = new ArrayList<>();
        hostHeaderValue.add(uri.getHost());
        headers.put("x-amz-pay-host", hostHeaderValue);

        if (header.isEmpty() || header == null)
            return headers;

        for (Map.Entry<String, String> entry : header.entrySet()) {
            final List<String> authHeaderValue = new ArrayList<>();
            authHeaderValue.add(entry.getValue());
            headers.put(entry.getKey(), authHeaderValue);
        }
        return headers;
    }

}
