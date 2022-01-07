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

import com.amazon.pay.api.types.Region;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URI;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RequestSigner.class, Util.class})
public class RequestSignerTest {
    private static final SignatureHelper signatureHelper = Mockito.mock(SignatureHelper.class);
    private PayConfiguration payConfiguration = new PayConfiguration();
    private URI uri;
    private String payload;
    private Map<String, List<String>> parameters = new HashMap<>();
    private String canonicalRequest;
    private String stringToSign;
    private String signature;
    private String signedHeaderString;
    private String authorizationHeader;
    private Map<String, List<String>> headers;
    private Map<String, String> postSignedHeadersMap;
    private String authToken;
    private Map<String, String> header;

    @Before
    public void setUp() throws Exception {
        System.setProperty("os.version", "4.9.93-0.1.ac.178.67.327.metal1.x86_64");
        System.setProperty("java.version", "1.8.0_172");
        System.setProperty("os.name", "Linux");
        PrivateKey mockKey = Mockito.mock(PrivateKey.class);
        mockStatic(Util.class);
        Mockito.when(Util.buildPrivateKey(Mockito.any(char[].class))).thenReturn(mockKey);
        payConfiguration.setRegion(Region.EU).setPublicKeyId("ADGUHQIH9988").setPrivateKey(new char[] {'p','r','i','v','a','t','e','K','e','y'});
        setUpMockValues();
    }

    private void setUpMockValues() throws Exception {
        authToken = "eyJhbGciOiJIbWFjU0hBMjU2IiwidHlwIjoiSldUIn0=";
        header = new HashMap<String, String>();
        uri = new URI("https://pay-api.amazon.eu/sandbox/v2/in-store/refund");
        payload = "payload";
        canonicalRequest = "POST\n" +
                "/sandbox/v2/in-store/refund\n" +
                "\n" +
                "accept:application/json\n" +
                "content-type:application/json\n" +
                "x-amz-pay-date:20180524T223710Z\n" +
                "x-amz-pay-host:pay-api.amazon.eu\n" +
                "x-amz-pay-region:EU\n" +
                "\n" +
                "accept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region\n" +
                "81dd99309152d21f2cef921656d3f57830fe9c36fe193af1b62de504e806aceb";

        stringToSign = ServiceConstants.AMAZON_SIGNATURE_ALGORITHM + "\n" +
                "227f8d4a6974e65a62ebe6648fab8666fe25f10dc2ec41fba9c439e633ba4b94";

        signature = "c062NjivoUW+TcHegKebFamCX8Cpmpmy6EiPmKwdpEuZZIpOHJYO";

        signedHeaderString = "accept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region";

        authorizationHeader = ServiceConstants.AMAZON_SIGNATURE_ALGORITHM + " PublicKeyId=ADGUHQIH9988, SignedHeaders=accept;content-type;x-amz-pay-date;x-amz-pay-host;x-amz-pay-region, Signature=c062NjivoUW+TcHegKebFamCX8Cpmpmy6EiPmKwdpEuZZIpOHJYO";

        headers = new HashMap<>();
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

        for (Map.Entry<String, String> entry : header.entrySet()) {
            final List<String> authHeaderValue = new ArrayList<>();
            authHeaderValue.add(entry.getValue());
            headers.put(entry.getKey(), authHeaderValue);
        }

        PowerMockito.whenNew(SignatureHelper.class).withAnyArguments().thenReturn(signatureHelper);
        Mockito.when(signatureHelper.createCanonicalRequest(Mockito.anyObject(), Mockito.anyString(), Mockito.anyMap(), Mockito.anyString(), Mockito.anyMap())).thenReturn(canonicalRequest);
        Mockito.when(signatureHelper.createStringToSign(Mockito.anyString())).thenReturn(stringToSign);
        Mockito.when(signatureHelper.generateSignature(Mockito.anyObject(), Mockito.anyObject())).thenReturn(signature);
        Mockito.when(signatureHelper.getSignedHeadersString(Mockito.anyMap())).thenReturn(signedHeaderString);
        Mockito.when(signatureHelper.createPreSignedHeaders(Mockito.anyObject(), Mockito.anyMap())).thenReturn(headers);

        postSignedHeadersMap = new HashMap<>();
        postSignedHeadersMap.put("accept", "application/json");
        postSignedHeadersMap.put("content-type", "application/json");
        postSignedHeadersMap.put("x-amz-pay-host", "pay-api.amazon.eu");
        postSignedHeadersMap.put("x-amz-pay-date", dateHeaderValue.get(0));
        postSignedHeadersMap.put("x-amz-pay-region", "EU");
        postSignedHeadersMap.put("authorization", authorizationHeader);
        postSignedHeadersMap.put("user-agent", "amazon-pay-api-sdk-java/" + ServiceConstants.APPLICATION_LIBRARY_VERSION + " (Java/1.8.0_172; Linux/4.9.93-0.1.ac.178.67.327.metal1.x86_64)");
    }

    @Test
    public void signRequestWithEmptyHeader() throws Exception {
        RequestSigner requestSigner = new RequestSigner(payConfiguration);
        Map<String, String> header = new HashMap<String, String>();
        Map<String, String> actualHeaders = requestSigner.signRequest(uri, "POST", parameters, payload, header);
        Assert.assertEquals(postSignedHeadersMap, actualHeaders);

        payConfiguration.setUserAgentRedaction(true);
        postSignedHeadersMap.put("user-agent", "amazon-pay-api-sdk-java/" + ServiceConstants.APPLICATION_LIBRARY_VERSION + " (Java/Redacted; Redacted/Redacted)");
        actualHeaders = requestSigner.signRequest(uri, "POST", parameters, payload, header);
        Assert.assertEquals(postSignedHeadersMap, actualHeaders);

    }

}
