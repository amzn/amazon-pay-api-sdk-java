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

import com.amazon.pay.api.exceptions.AmazonPayClientException;

import java.net.URI;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestSigner {

    final private PayConfiguration payConfiguration;
    final private SignatureHelper signatureHelper;
    final private PrivateKey privateKey;

    public RequestSigner(final PayConfiguration payConfiguration) throws AmazonPayClientException {
        checkIfConfigParametersAreSet(payConfiguration);
        this.payConfiguration = payConfiguration;
        signatureHelper = new SignatureHelper(payConfiguration);
        privateKey = payConfiguration.getPrivateKey();
    }

    /**
     * Signs the request provided and returns the signed headers map
     * @param uri The uri that needs to be executed
     * @param httpMethodName the HTTP request method(GET,PUT,POST etc) to be used
     * @param queryParameters the query parameters map
     * @param requestPayload the payload to be sent with the request
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return a map of signed headers
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public Map<String, String> signRequest(final URI uri,
                                           final String httpMethodName,
                                           final Map<String, List<String>> queryParameters,
                                           final String requestPayload,
                                           final Map<String, String> header) throws AmazonPayClientException {
        final String publicKeyId = payConfiguration.getPublicKeyId();
        final Map<String, List<String>> preSignedHeaders = signatureHelper.createPreSignedHeaders(uri, header);
        final String userAgent = buildUserAgentHeader();

        String signature = null;
        try {
            final String canonicalRequest = signatureHelper.createCanonicalRequest(uri, httpMethodName, queryParameters, requestPayload, preSignedHeaders);
            final String stringToSign = signatureHelper.createStringToSign(canonicalRequest);
            signature = signatureHelper.generateSignature(stringToSign, privateKey);

        } catch (NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | InvalidKeyException
                | SignatureException e) {
            throw new AmazonPayClientException(e.getMessage(), e);
        }

        final String authorizationHeader = buildAuthorizationHeader(publicKeyId, preSignedHeaders, signature);

        Map<String, String> postSignedHeadersMap = new HashMap<>();

        for (String key : preSignedHeaders.keySet()) {
            postSignedHeadersMap.put(key.toLowerCase(), preSignedHeaders.get(key).get(0));
        }
        postSignedHeadersMap.put("authorization", authorizationHeader);
        postSignedHeadersMap.put("user-agent", userAgent);

        return postSignedHeadersMap;
    }

    /**
     * Builds the user agent header
     * @return the user agent string
     */
    private String buildUserAgentHeader() {
        final String javaVersion = payConfiguration.isUserAgentRedaction() ? ServiceConstants.REDACTED : Util.JAVA_VERSION;
        final String osName = payConfiguration.isUserAgentRedaction() ? ServiceConstants.REDACTED : Util.OS_NAME;
        final String osVersion = payConfiguration.isUserAgentRedaction() ? ServiceConstants.REDACTED : Util.OS_VERSION;

        final StringBuilder userAgentBuilder = new StringBuilder(ServiceConstants.GITHUB_SDK_NAME).append("/")
                .append(ServiceConstants.APPLICATION_LIBRARY_VERSION)
                .append(" (").append("Java/").append(javaVersion)
                .append("; ").append(osName)
                .append("/").append(osVersion).append(")");

        return userAgentBuilder.toString();
    }

    /**
     * Builds the authorization header
     * @param publicKeyId the public key id from the PayConfiguration
     * @param preSignedHeaders the set of pre signed headers
     * @param signature the generated signature
     * @return the authorization string
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    private String buildAuthorizationHeader(String publicKeyId,
                                            Map<String, List<String>> preSignedHeaders,
                                            String signature) throws AmazonPayClientException {

        // The parameters being null should never happen at this point, because that
        // case should be caught by other exceptions upstream.
        // Including this as a final sanity check.
        if (publicKeyId == null || preSignedHeaders == null || signature == null) {
            throw new AmazonPayClientException("Invalid parameter while constructing authorization header");
        }

        final StringBuilder authorizationBuilder = new StringBuilder(ServiceConstants.AMAZON_SIGNATURE_ALGORITHM)
                .append(" PublicKeyId=").append(publicKeyId).append(", ").append("SignedHeaders=")
                .append(signatureHelper.getSignedHeadersString(preSignedHeaders))
                .append(", Signature=").append(signature);

        return authorizationBuilder.toString();
    }

    /**
     * Helper method to check if required values are set.
     * @param payConfiguration the PayConfiguration object
     * @throws IllegalArgumentException If required values are missing.
     */
    private void checkIfConfigParametersAreSet(PayConfiguration payConfiguration) {
        if (payConfiguration.getRegion() == null || payConfiguration.getRegion().toString().isEmpty()) {
            generateException(ServiceConstants.REGION);
        }
        if (payConfiguration.getPrivateKey() == null) {
            generateException(ServiceConstants.PRIVATE_KEY);
        }
        if (payConfiguration.getPublicKeyId() == null || payConfiguration.getPublicKeyId().isEmpty()) {
            generateException(ServiceConstants.PUBLIC_KEY_ID);
        }
    }

    /**
     * To throw an exception if required values in the PayConfiguration are missing
     * @param parameter
     */
    private void generateException(String parameter) {
        throw new IllegalArgumentException(parameter +
                " is not set in the PayConfiguration, this is a required parameter");
    }

}
