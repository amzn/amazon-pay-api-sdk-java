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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.commons.lang.StringUtils;

import com.amazon.pay.api.exceptions.AmazonPayClientException;
import com.amazon.pay.api.types.AmazonSignatureAlgorithm;

import org.json.JSONException;
import org.json.JSONObject;

public class AmazonPayClient {
    final protected PayConfiguration payConfiguration;
    final protected RequestSigner requestSigner;

    public AmazonPayClient(final PayConfiguration payConfiguration) throws AmazonPayClientException {
        this.payConfiguration = payConfiguration;
        requestSigner = new RequestSigner(payConfiguration);
    }

    /**
     * The Delivery Tracker operation is used to track the delivery status
     *
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the deliveryTracker service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse deliveryTracker(final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI deliveryTrackerURI = Util.getServiceURI(payConfiguration, ServiceConstants.DELIVERY_TRACKERS);
        return callAPI(deliveryTrackerURI, "POST", null, payload.toString(), header);
    }

    /**
     * The get Authorization Token operation is used to obtain retrieve a delegated authorization token
     *  used in order to make API calls on behalf of a merchant.
     *  This token is needed to make delegated calls.
     * Important: getAuthorizationToken() requires a Client configured to use the live environment.
     *
     * @param  mwsAuthToken MWS Authorization Token previously shared by Merchant to the Solution Provider
     * @param  merchantId Merchant ID that generated the MWS Authorization Token
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the getAuthorizationToken service API, as returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     * @see AmazonPayClient
     * @see PayConfiguration
     * @see com.amazon.pay.api.types.Environment
     */
    public AmazonPayResponse getAuthorizationToken(final String mwsAuthToken, final String merchantId, final Map<String, String> header) throws AmazonPayClientException {
        final URI authorizationTokenURI = Util.getServiceURI(payConfiguration, ServiceConstants.AUTHORIZATION_TOKEN);
        final URI getAuthorizationTokenURI = authorizationTokenURI.resolve(authorizationTokenURI.getPath() + "/" + mwsAuthToken + "?merchantId=" + merchantId);
        final Map<String, List<String>> queryParametersMap = new HashMap<>();
        ArrayList<String> auxList = new ArrayList<String>();
        auxList.add(merchantId);
        queryParametersMap.put("merchantId", auxList);
        return callAPI(getAuthorizationTokenURI, "GET", queryParametersMap, "", header);
    }


    /**
     * The Delivery Tracker operation is used to track the delivery status
     *
     * @param payload JSONObject request body
     * @return The response from the deliveryTracker service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse deliveryTracker(final JSONObject payload) throws AmazonPayClientException {
        return deliveryTracker(payload, null);
    }


    /**
     * generateButtonSignature is a convenience method to assist the developer in generating static signatures
     * that can be used by Checkout v2's amazon.Pay.renderButton method.  Unlike API call signatures, the
     * system timestamp is not part of the signing logic, so these signatures do not expire.
     *
     * @param payload The payloadJSON attribute from createCheckoutSessionConfig object used by amazon.Pay.renderButton method from checkout.js
     * @return String signature attribute value for createCheckoutSessionConfig object on your website frontend
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public String generateButtonSignature(final JSONObject payload) throws AmazonPayClientException {
        return generateButtonSignature(payload.toString());
    }


    /**
     * generateButtonSignature is a convenience method to assist the developer in generating static signatures
     * that can be used by Checkout v2's amazon.Pay.renderButton method.  Unlike API call signatures, the
     * system timestamp is not part of the signing logic, so these signatures do not expire.
     *
     * @param payload The payloadJSON attribute from createCheckoutSessionConfig object used by amazon.Pay.renderButton method from checkout.js
     * @return String signature attribute value for createCheckoutSessionConfig object on your website frontend
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public String generateButtonSignature(final String payload) throws AmazonPayClientException {
        String signature = null;
        final SignatureHelper signatureHelper = new SignatureHelper(payConfiguration);
        final AmazonSignatureAlgorithm algorithm = payConfiguration.getAlgorithm();
        try {
            final String stringToSign = signatureHelper.createStringToSign(payload, algorithm.getName());
            signature =  signatureHelper.generateSignature(stringToSign, payConfiguration.getPrivateKey(), algorithm);
        } catch (NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | InvalidKeyException
                | SignatureException e) {
            throw new AmazonPayClientException(e.getMessage(), e);
        }
        return signature;
    }


    /**
     * API to process the request and return the
     *
     * @param uri             The uri that needs to be executed
     * @param httpMethodName  the HTTP request method(GET,PUT,POST etc) to be used
     * @param queryParameters the query parameters map
     * @param request         the payload to be sent with the request
     * @param header          the header of the solution provider
     * @return response of type AmazonPayResponse
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse callAPI(final URI uri,
                                     final String httpMethodName,
                                     final Map<String, List<String>> queryParameters,
                                     final String request,
                                     final Map<String, String> header) throws AmazonPayClientException {
        Map<String, String> postSignedHeaders;

        postSignedHeaders = requestSigner.signRequest(uri, httpMethodName, queryParameters, request, header);
        return processRequest(uri, postSignedHeaders, request, httpMethodName);
    }

    /**
     * Helper method to send the request and also retry in case the request is throttled
     *
     * @param uri               the uri to be executed
     * @param postSignedHeaders the signed headers
     * @param payload           the payload to be sent with the request
     * @param httpMethodName    the HTTP request method(GET,PUT,POST etc) to be used
     * @return the AmazonPayResponse
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
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
            if (response.get(ServiceConstants.RESPONSE_STRING) != null) {
                // Converting the response string into a JSONObject
                rawResponseObject = response.get(ServiceConstants.RESPONSE_STRING);
                if(!StringUtils.isEmpty(response.get(ServiceConstants.RESPONSE_STRING))) {
                    jsonResponse = new JSONObject(response.get(ServiceConstants.RESPONSE_STRING));
                }
            }
        } catch (InterruptedException | JSONException e) {
            throw new AmazonPayClientException(e.getMessage(), e);
        }
        responseObject.setResponse(jsonResponse);
        responseObject.setRawResponse(rawResponseObject);
        responseObject.setRequestId(response.get(ServiceConstants.REQUEST_ID));

        return responseObject;
    }

    /**
     * Helper method to post the request
     *
     * @param uri            the uri to be executed
     * @param headers        the signed headers
     * @param payload        the payload ot be sent with the request
     * @param httpMethodName the HTTP request method(GET,PUT,POST etc) to be used
     * @return the response and response code
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    private List<String> sendRequest(final URI uri,
                                     final Map<String, String> headers,
                                     final String payload,
                                     final String httpMethodName) throws AmazonPayClientException {
        final List<String> result = new ArrayList<>();
        final StringBuffer response = new StringBuffer();
        String requestId = null;
        int responseCode = 0;
        try (final CloseableHttpClient client = Optional.ofNullable(payConfiguration.getProxySettings()).isPresent()
                ? Util.getCloseableHttpClientWithProxy(payConfiguration.getProxySettings(), payConfiguration)
                    : Util.getHttpClientWithConnectionPool(payConfiguration)) {
            final HttpUriRequest httpUriRequest = Util.getHttpUriRequest(uri, httpMethodName, payload);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpUriRequest.addHeader(entry.getKey(), entry.getValue());
            }
            final HttpResponse responses = client.execute(httpUriRequest);
            responseCode = responses.getStatusLine().getStatusCode();
            if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                requestId = responses.getFirstHeader(ServiceConstants.X_AMZ_PAY_REQUEST_ID).toString();
                String inputLine;
                try (final BufferedReader in = new BufferedReader(
                        new InputStreamReader(responses.getEntity().getContent(), Util.DEFAULT_ENCODING))) {
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine).append(System.lineSeparator());
                    }
                }
            } else {
                response.append(EntityUtils.toString(responses.getEntity()));
            }
        } catch (IOException exception) {
            throw new AmazonPayClientException(exception.getMessage(), exception);
        }
        result.add(String.valueOf(responseCode));
        result.add(response.toString());
        result.add(requestId);
        return result;
    }

}
