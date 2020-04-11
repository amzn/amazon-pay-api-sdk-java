package com.amazon.pay.api;

import com.amazon.pay.api.exceptions.AmazonPayClientException;
import com.amazon.pay.api.types.Region;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
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
        JSONArray allTestCases = (JSONArray) JSONSerializer.toJSON(fileTestContent);
        for (Object test : allTestCases)
            TEST_CASES.add((JSONObject) test);
    }

    @Test
    public void ValidateAllTestCases() throws URISyntaxException, NoSuchAlgorithmException, AmazonPayClientException {
        for (JSONObject testCase : TEST_CASES) {
            String name = testCase.getString("name");
            String method = testCase.getString("method");
            URI uri = new URI(testCase.getString("uri"));
            Map<String, List<String>> queryParams = getParameters((JSONObject) testCase.get("parameters"));
            String payload = testCase.getString("payload");
            Map<String, List<String>> preSignedHeaders = mockedPreSignedHeaders(uri);

            String actualCanonicalRequest = signatureHelper.createCanonicalRequest(uri, method, queryParams, payload, preSignedHeaders);
            String expectedCanonicalRequest = testCase.getString("canonicalRequest");


            String actualStringToSign = signatureHelper.createStringToSign(actualCanonicalRequest);
            String expectedStringToSign = testCase.getString("stringToSign");

            Assert.assertEquals("Test Case Name : " + name, expectedCanonicalRequest, actualCanonicalRequest);
            Assert.assertEquals("Test Case Name : " + name, expectedStringToSign, actualStringToSign);

        }

    }

    private Map<String, List<String>> mockedPreSignedHeaders(URI uri) throws URISyntaxException {
        Map<String, List<String>> headers = new HashMap<>();

        List<String> acceptHeaderValue = new ArrayList<>();
        acceptHeaderValue.add("application/json");
        headers.put("accept", acceptHeaderValue);

        List<String> contentHeaderValue = new ArrayList<>();
        contentHeaderValue.add("application/json");
        headers.put("content-type", contentHeaderValue);

        List<String> regionHeaderValue = new ArrayList<>();
        regionHeaderValue.add(Region.EU.toString());
        headers.put("x-amz-pay-region", regionHeaderValue);

        List<String> dateHeaderValue = new ArrayList<>();
        dateHeaderValue.add("20180524T223710Z");
        headers.put("x-amz-pay-date", dateHeaderValue);

        List<String> hostHeaderValue = new ArrayList<>();
        hostHeaderValue.add("pay-api.amazon.eu");
        headers.put("x-amz-pay-host", hostHeaderValue);

        return headers;
    }

    private HashMap<String, List<String>> getParameters(JSONObject jsonObject) {
        HashMap<String, List<String>> parameters = new HashMap<>();
        for (Object key : jsonObject.keySet()) {
            JSONArray vals = jsonObject.getJSONArray((String) key);
            ArrayList<String> l = new ArrayList<>();
            for (Object val : vals.toArray()) {
                l.add((String) val);
            }
            parameters.put((String) key, l);
        }

        return parameters;
    }
}
