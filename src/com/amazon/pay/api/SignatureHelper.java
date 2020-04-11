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

import com.amazon.pay.api.exceptions.AmazonPayClientException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import java.net.URI;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class SignatureHelper {
    private final PayConfiguration payConfiguration;
    private final String LINE_SEPARATOR = "\n";
    private final int SALT_LENGTH = 20;
    private final int TRAILER_FIELD = 1;

    public SignatureHelper(final PayConfiguration payConfiguration) {
        this.payConfiguration = payConfiguration;
    }

    /**
     * Creates a string that includes the information from the request in a standardized(canonical) format.
     * @param uri The uri that needs to be executed
     * @param httpMethodName the HTTP request method(GET,PUT,POST etc) to be used
     * @param parameters the query parameters map
     * @param requestPayload the payload to be sent with the request
     * @param preSignedHeaders the mandatory headers required
     * @return a canonical request
     * @throws AmazonPayClientException
     * algorithm requested is not available in the environment
     */
    public String createCanonicalRequest(final URI uri,
                                         final String httpMethodName,
                                         final Map<String, List<String>> parameters,
                                         final String requestPayload,
                                         final Map<String, List<String>> preSignedHeaders) throws AmazonPayClientException {
        final String path = uri.getPath();
        final StringBuilder canonicalRequestBuilder = new StringBuilder(httpMethodName);

        try {
            canonicalRequestBuilder.append(LINE_SEPARATOR)
                    .append(getCanonicalizedURI(path))
                    .append(LINE_SEPARATOR)
                    .append(getCanonicalizedQueryString(parameters))
                    .append(LINE_SEPARATOR)
                    .append(getCanonicalizedHeaderString(preSignedHeaders))
                    .append(LINE_SEPARATOR)
                    .append(getSignedHeadersString(preSignedHeaders))
                    .append(LINE_SEPARATOR)
                    .append(hashThenHexEncode(requestPayload));
        } catch (NoSuchAlgorithmException e) {
            throw new AmazonPayClientException(e.getMessage(), e);
        }

        return canonicalRequestBuilder.toString();
    }

    /**
     * Creates the string that is going to be signe
     * @param canonicalRequest The canonical request generated using the createCanonicalRequest() method
     * @return the string to be signed
     * @throws NoSuchAlgorithmException exception thrown when the cryptographic
     * algorithm requested is not available in the environment
     */
    public String createStringToSign(final String canonicalRequest) throws NoSuchAlgorithmException {
        final String hashedCanonicalRequest = hashThenHexEncode(canonicalRequest);

        final StringBuilder stringToSignBuilder = new StringBuilder(ServiceConstants.AMAZON_SIGNATURE_ALGORITHM);
        stringToSignBuilder.append(LINE_SEPARATOR)
                .append(hashedCanonicalRequest);

        return stringToSignBuilder.toString();
    }

    /**
     * Generates a signature for the string passed in
     * @param stringToSign the string to be signed
     * @param privateKey the private key to use for signing
     * @return the signature
     * @throws NoSuchAlgorithmException exception thrown when the cryptographic
     * algorithm requested is not available in the environment
     * @throws NoSuchProviderException exception thrown when the security
     * provider requested is not available in the environment
     * @throws InvalidAlgorithmParameterException exception for invalid algorithm parameters
     * @throws InvalidKeyException exception for invalid keys
     * @throws SignatureException signature exception
     */
    public String generateSignature(final String stringToSign, final PrivateKey privateKey) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException,
            InvalidKeyException, SignatureException {
        final Signature signature = Signature.getInstance(ServiceConstants.SIGNATURE_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
        final MGF1ParameterSpec mgf1ParameterSpec = new MGF1ParameterSpec(ServiceConstants.HASH_ALGORITHM);
        final PSSParameterSpec pssParameterSpec = new PSSParameterSpec(ServiceConstants.HASH_ALGORITHM,
                ServiceConstants.MASK_GENERATION_FUNCTION, mgf1ParameterSpec, SALT_LENGTH, TRAILER_FIELD);
        signature.setParameter(pssParameterSpec);
        signature.initSign(privateKey);
        signature.update(stringToSign.getBytes());

        return new String(Base64.encode(signature.sign()));
    }

    /**
     * Creates the mandatory headers required in the request
     * @param uri the uri to be executed
     * @return a map of mandatory headers
     */
    public Map<String, List<String>> createPreSignedHeaders(final URI uri, final Map<String, String> header) throws AmazonPayClientException {
        final Map<String, List<String>> headers = new HashMap<>();

        //List of Headers added by Amazon Pay
        final List<String> acceptHeaderValue = new ArrayList<>();
        acceptHeaderValue.add("application/json");
        headers.put("accept", acceptHeaderValue);

        final List<String> contentHeaderValue = new ArrayList<>();
        contentHeaderValue.add("application/json");
        headers.put("content-type", contentHeaderValue);

        final List<String> regionHeaderValue = new ArrayList<>();
        regionHeaderValue.add(payConfiguration.getRegion().toString());
        headers.put("x-amz-pay-region", regionHeaderValue);

        final List<String> dateHeaderValue = new ArrayList<>();
        dateHeaderValue.add(Util.getFormattedTimestamp());
        headers.put("x-amz-pay-date", dateHeaderValue);

        final List<String> hostHeaderValue = new ArrayList<>();
        hostHeaderValue.add(uri.getHost());
        headers.put("x-amz-pay-host", hostHeaderValue);

        //If no header is provided by the merchant, return the headers added by Amazon Pay
        if (header == null || header.isEmpty()) {
            return headers;
        }

        //Executes only if header is sent by the merchant.
        //Converts the Merchant's header to lowercase for case insensitivity.
        final Map<String, String> lowerCaseUserHeader = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            lowerCaseUserHeader.put(entry.getKey().toLowerCase(), entry.getValue());
        }

        final Set<String> headersKeySet = headers.keySet();

        //Checks if the merchant's header is already present in headers added by Amazon Pay
        //If present, override headers by Amazon Pay with Merchant provided header value
        //Adds any new header sent by Merchant
        for (Map.Entry<String, String> entry : lowerCaseUserHeader.entrySet()) {
            if (headersKeySet.contains(entry.getKey())) {
                final List<String> alreadyPresentHeaderValue = new ArrayList<>();
                alreadyPresentHeaderValue.add(entry.getValue());
                headers.put(entry.getKey(), alreadyPresentHeaderValue);
            } else {
                final List<String> newHeaderValue = new ArrayList<>();
                newHeaderValue.add(entry.getValue());
                headers.put(entry.getKey(), newHeaderValue);
            }
        }

        return headers;
    }

    /**
     * Generates a string that is a list of headers names that are included in the canonical headers.
     * This is to identify which headers are a part of the signing process.
     * @param preSignedHeaders the mandatory header
     * @return the string of signed headers
     */
    public String getSignedHeadersString(final Map<String, List<String>> preSignedHeaders) {
        final List<String> sortedHeaders = new ArrayList<>(preSignedHeaders.keySet());
        Collections.sort(sortedHeaders, String.CASE_INSENSITIVE_ORDER);

        final StringBuilder buffer = new StringBuilder();
        for (String header : sortedHeaders) {
            if (buffer.length() > 0)
                buffer.append(";");
            buffer.append(Util.lowerCase(header));
        }

        return buffer.toString();
    }

    /**
     * Generates a canonical headers string that consists of a list of all HTTP headers
     * that are included with the request.
     * @param preSignedHeaders the mandatory headers
     * @return the canonical header string
     */
    public String getCanonicalizedHeaderString(final Map<String, List<String>> preSignedHeaders) {
        final List<String> sortedHeaders = new ArrayList<>(preSignedHeaders.keySet());
        Collections.sort(sortedHeaders, String.CASE_INSENSITIVE_ORDER);

        final Map<String, List<String>> requestHeaders = preSignedHeaders;
        final StringBuilder buffer = new StringBuilder();
        for (String header : sortedHeaders) {
            String key = Util.lowerCase(header);
            List<String> values = requestHeaders.get(header);
            StringBuilder headerValue = new StringBuilder();
            for (String value : values) {
                if (headerValue != null) {
                    if (headerValue.length() > 0) {
                        headerValue.append(",");
                    }
                    headerValue.append(value.trim().replaceAll("\\s", " "));
                }
            }
            buffer.append(key.trim().replaceAll("\\s", " "));
            buffer.append(":");
            if (headerValue != null) {
                buffer.append(headerValue);
            }
            buffer.append(LINE_SEPARATOR);
        }

        return buffer.toString();
    }

    /**
     * Generates a canonical string that consists of all the query parameters
     * @param parameters the query parameters of the request
     * @return the canonical query string
     * @throws AmazonPayClientException
     */
    public String getCanonicalizedQueryString(final Map<String, List<String>> parameters) throws AmazonPayClientException {
        final SortedMap<String, List<String>> sorted = new TreeMap<>();

        /**
         * Signing protocol expects the param values also to be sorted after url
         * encoding in addition to sorted parameter names.
         */
        if (parameters != null) {
            for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
                final String encodedParamName = Util.urlEncode(
                        entry.getKey(), false);
                final List<String> paramValues = entry.getValue();
                final List<String> encodedValues = new ArrayList<String>(
                        paramValues.size());
                for (String value : paramValues) {
                    encodedValues.add(Util.urlEncode(value, false));
                }
                Collections.sort(encodedValues);
                sorted.put(encodedParamName, encodedValues);

            }
        }

        final StringBuilder result = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : sorted.entrySet()) {
            for (String value : entry.getValue()) {
                if (result.length() > 0) {
                    result.append("&");
                }
                result.append(entry.getKey())
                        .append("=")
                        .append(value);
            }
        }

        return result.toString();
    }


    /**
     * Generates a canonical URI string
     * @param path the path of the uri provided
     * @return canonical URI string
     * @throws AmazonPayClientException
     */
    private String getCanonicalizedURI(final String path) throws AmazonPayClientException {
        if (path == null || path.isEmpty()) {
            return "/";
        } else {
            String value = Util.urlEncode(path, true);
            if (value.startsWith("/")) {
                return value;
            } else {
                return "/".concat(value);
            }
        }
    }

    /**
     * Generates a Hex encoded string from a hashed value of the payload string
     * @param requestPayload the payload to be hashed
     * @return the hashed payload string
     * @throws NoSuchAlgorithmException exception thrown when the cryptographic
     * algorithm requested is not available in the environment
     */
    private String hashThenHexEncode(final String requestPayload) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance(ServiceConstants.HASH_ALGORITHM);
        md.update(requestPayload.getBytes());
        final byte[] digest = md.digest();

        final String contentSha256 = new String((Hex.encode(digest)));
        return contentSha256;
    }
}
