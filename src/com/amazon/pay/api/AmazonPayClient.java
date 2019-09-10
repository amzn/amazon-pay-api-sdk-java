/**
 * Copyright 2018-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazon.pay.api;

import com.amazon.pay.api.exceptions.AmazonPayClientException;
import com.amazon.pay.api.PayConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

public class AmazonPayClient {
    final private PayConfiguration payConfiguration;
    final private RequestSigner requestSigner;
    final private Map<String, List<String>> queryParametersMap = new HashMap<>();

    public AmazonPayClient(final PayConfiguration payConfiguration) throws AmazonPayClientException {
        this.payConfiguration = payConfiguration;
        requestSigner = new RequestSigner(payConfiguration);
        allowPatch();
    }

    /**
     * The Delivery Tracker operation is used to track the delivery status
     * @param payload
     * @param header
     * @return The response from the deliveryTracker service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse deliveryTracker(final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI deliveryTrackerURI = Util.getServiceURI(payConfiguration, ServiceConstants.DELIVERY_TRACKERS);
        return callAPI(deliveryTrackerURI, "POST", queryParametersMap, payload.toString(), header);
    }

    /**
     * The Delivery Tracker operation is used to track the delivery status
     * @param payload
     * @return The response from the deliveryTracker service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse deliveryTracker(final JSONObject payload) throws AmazonPayClientException {
        return deliveryTracker(payload, null);
    }

    /**
     * API to process the request and return the
     * @param uri The uri that needs to be executed
     * @param httpMethodName the HTTP request method(GET,PUT,POST etc) to be used
     * @param queryParameters the query parameters map
     * @param request the payload to be sent with the request
     * @param header the header of the solution provider
     * @return response of type AmazonPayResponse
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse callAPI(final URI uri,
                                     final String httpMethodName,
                                     final Map<String, List<String>> queryParameters,
                                     final String request,
                                     final Map<String,String> header) throws AmazonPayClientException {
        Map<String, String> postSignedHeaders;
        postSignedHeaders = requestSigner.signRequest(uri, httpMethodName, queryParameters, request, header);
        return processRequest(uri, postSignedHeaders, request, httpMethodName);
    }

    /**
     * Helper method to send the request and also retry in case the request is throttled
     * @param uri the uri to be executed
     * @param postSignedHeaders the signed headers
     * @param payload the payload to be sent with the request
     * @param httpMethodName the HTTP request method(GET,PUT,POST etc) to be used
     * @return the AmazonPayResponse
     * @throws AmazonPayClientException
     */
    private AmazonPayResponse processRequest(final URI uri,
                                             final Map<String, String> postSignedHeaders,
                                             final String payload,
                                             final String httpMethodName) throws AmazonPayClientException {
        List<String> response;
        String rawResponseObject = null;
        JSONObject jsonResponse = null;

        final AmazonPayResponse responseObject = new AmazonPayResponse();
        responseObject.setUrl(uri);
        responseObject.setMethod(httpMethodName);
        responseObject.setRawRequest(payload);
        responseObject.setHeaders(postSignedHeaders);
        try {
            long millisBefore = System.currentTimeMillis();
            response = sendRequest(uri, postSignedHeaders, payload, httpMethodName);
            int statusCode = Integer.parseInt(response.get(ServiceConstants.RESPONSE_STATUS_CODE));
            int retry = 0;
            // Check for service errors
            while (ServiceConstants.serviceErrors.containsValue(statusCode) &&
                    retry < payConfiguration.getMaxRetries()) {
                retry++;
                //retry request maxRetries number of times
                long waitTime = Util.getExponentialWaitTime(retry);
                Thread.sleep(waitTime);

                response = sendRequest(uri, postSignedHeaders, payload, httpMethodName);
                statusCode = Integer.parseInt(response.get(ServiceConstants.RESPONSE_STATUS_CODE));
            }
            responseObject.setRetries(retry);
            responseObject.setStatus(statusCode);
            responseObject.setDuration(System.currentTimeMillis() - millisBefore);
        }
        catch (InterruptedException e) {
            throw new AmazonPayClientException(e.getMessage(), e);
        }

        if (response.get(ServiceConstants.RESPONSE_STRING) != null) {
            // Converting the response string into a JSONObject
            rawResponseObject = response.get(ServiceConstants.RESPONSE_STRING);
            jsonResponse = JSONObject.fromObject(response.get(ServiceConstants.RESPONSE_STRING));
        }
        responseObject.setResponse(jsonResponse);
        responseObject.setRawResponse(rawResponseObject);
        responseObject.setRequestId(response.get(ServiceConstants.REQUEST_ID));

        return  responseObject;
    }

    /**
     * Helper method to post the request
     * @param uri the uri to be executed
     * @param headers the signed headers
     * @param payload the payload ot be sent with the request
     * @param httpMethodName the HTTP request method(GET,PUT,POST etc) to be used
     * @return the response and response code
     * @throws AmazonPayClientException
     */
    private List<String> sendRequest(final URI uri,
                                     final Map<String, String> headers,
                                     final String payload,
                                     final String httpMethodName) throws AmazonPayClientException {
        List<String> result = new ArrayList<>();
        HttpURLConnection conn = null;
        StringBuffer response = new StringBuffer();
        String requestId = null;
        int responseCode = 0;
        try {
            conn = (HttpURLConnection)uri.toURL().openConnection();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
            conn.setRequestMethod(httpMethodName);
            conn.setDoInput(true);
            if (payload == null || payload.isEmpty()) {
                conn.setDoOutput(false);
            }
            else {
                conn.setDoOutput(true);
                try (OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream())) {
                    out.write(payload);
                    out.flush();
                }
                catch (IOException e) {
                    throw new AmazonPayClientException(e.getMessage(), e);
                }
            }
            responseCode = conn.getResponseCode();
            requestId = conn.getHeaderField("X-Amz-Pay-Request-Id");
             String inputLine;
            if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine).append("\n");
                    }
                }
                 catch (IOException e) {
                    throw new AmazonPayClientException(e.getMessage(), e);
                }
            }
            else {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine).append("\n");
                    }
                }
                 catch (IOException e) {
                    throw new AmazonPayClientException(e.getMessage(), e);
                }
            }
        }
        catch (IOException e) {
            throw new AmazonPayClientException(e.getMessage(), e);
        }
        result.add(String.valueOf(responseCode));
        result.add(response.toString());
        result.add(requestId);
        return result;
    }

    /**
     * The allowPatch operation is used to call PATCH requests.
     * @throws IllegalStateException
     */
    private static void allowPatch() {
        try {
            final Field methodsField = HttpURLConnection.class.getDeclaredField("methods");

            final Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

            methodsField.setAccessible(true);
            final String[] oldMethods = (String[]) methodsField.get(null);
            final Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));

            methodsSet.add("PATCH");
            final String[] newMethods = methodsSet.toArray(new String[0]);

            methodsField.set(null, newMethods);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

}
