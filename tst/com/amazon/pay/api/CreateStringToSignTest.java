package com.amazon.pay.api;

import com.amazon.pay.api.exceptions.AmazonPayClientException;
import com.amazon.pay.api.types.Region;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CreateStringToSignTest {
    private final static String TEST_FILE = System.getProperty("user.dir") + "/tst/com/amazon/pay/api/testdata.js";
    private final static List<JSONObject> TEST_CASES = new ArrayList<>();
    private final static PayConfiguration payConfiguration = new PayConfiguration().setRegion(Region.EU).setPublicKeyId("");
    private final static SignatureHelper signatureHelper = new SignatureHelper(payConfiguration);

    @BeforeClass
    public static void readTestCasesFromFile() throws Throwable {
        String fileTestContent = new String(Files.readAllBytes(Paths.get(TEST_FILE)), StandardCharsets.UTF_8);
        JSONArray allTestCases = new JSONArray(fileTestContent);
        for (int i = 0; i < allTestCases.length(); i++)
            TEST_CASES.add((JSONObject) allTestCases.getJSONObject(i));
    }

    @Test
    public void ValidateAllTestCases() throws URISyntaxException, NoSuchAlgorithmException, AmazonPayClientException, JSONException {
        for (JSONObject testCase : TEST_CASES) {
            String name = testCase.optString("name");
            String method = testCase.optString("method");
            URI uri = new URI(testCase.optString("uri"));
            Map<String, List<String>> queryParams = getParameters((JSONObject) testCase.get("parameters"));
            String payload = testCase.optString("payload");
            String stringToSign = testCase.optString("stringToSign");
            Map<String, List<String>> preSignedHeaders = mockedPreSignedHeaders(uri);
            String algorithm = stringToSign.startsWith("AMZN-PAY-RSASSA-PSS-V2") ?
                    stringToSign.substring(0,22) : stringToSign.substring(0,19);

            String actualCanonicalRequest = signatureHelper.createCanonicalRequest(uri, method, queryParams, payload, preSignedHeaders);
            String expectedCanonicalRequest = testCase.optString("canonicalRequest");

            String actualStringToSign = signatureHelper.createStringToSign(actualCanonicalRequest,algorithm);
            String expectedStringToSign = testCase.optString("stringToSign");

            Assert.assertEquals("Test Case Name : " + name, expectedCanonicalRequest, actualCanonicalRequest);
            Assert.assertEquals("Test Case Name : " + name, expectedStringToSign, actualStringToSign);

        }

    }

    private Map<String, List<String>> mockedPreSignedHeaders(final URI uri) throws URISyntaxException {
        Map<String, List<String>> headers = new HashMap<>();

        List<String> acceptHeaderValue = new ArrayList<String>(){
            {
                add("application/json");
            }
        };
        headers.put("accept", acceptHeaderValue);

        List<String> contentHeaderValue = new ArrayList<String>(){
            {
                add("application/json");
            }
        };
        headers.put("content-type", contentHeaderValue);

        List<String> regionHeaderValue = new ArrayList<String>(){
            {
                add(Region.EU.toString());
            }
        };
        headers.put("x-amz-pay-region", regionHeaderValue);

        List<String> dateHeaderValue = new ArrayList<String>(){
            {
                add("20180524T223710Z");
            }
        };
        headers.put("x-amz-pay-date", dateHeaderValue);

        List<String> hostHeaderValue = new ArrayList<String>(){
            {
                add("pay-api.amazon.eu");
            }
        };
        headers.put("x-amz-pay-host", hostHeaderValue);

        return headers;
    }

    private HashMap<String, List<String>> getParameters(final JSONObject jsonObject) throws JSONException {
        HashMap<String, List<String>> parameters = new HashMap<>();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            JSONArray vals = jsonObject.getJSONArray((String) key);
            ArrayList<String> l = new ArrayList<>();
            for (int i = 0; i < vals.length(); i++) {
                l.add(vals.getString(i));
            }
            parameters.put((String) key, l);
        }

        return parameters;
    }
}
