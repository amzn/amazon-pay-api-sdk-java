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
import com.amazon.pay.api.types.Environment;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static final String JAVA_VERSION = System.getProperty("java.version");
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String OS_VERSION = System.getProperty("os.version");
    public static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Regex which matches any of the sequences that we need to fix up after
     * URLEncoder.encode().
     */
    private static final Pattern ENCODED_CHARACTERS_PATTERN;

    static {
        StringBuilder pattern = new StringBuilder();

        pattern
                .append(Pattern.quote("+"))
                .append("|")
                .append(Pattern.quote("*"))
                .append("|")
                .append(Pattern.quote("%7E"))
                .append("|")
                .append(Pattern.quote("%2F"));

        ENCODED_CHARACTERS_PATTERN = Pattern.compile(pattern.toString());
    }

    /**
     * Generates a url encoded string from the given string
     * @param value the string to be encoded
     * @param path to determine if the given value is a string or not
     * @return the url encoded string
     * @throws AmazonPayClientException
     */
    public static String urlEncode(String value, final boolean path) throws AmazonPayClientException {
        if (value == null) {
            return "";
        }

        try {
            if (path) {
                value = value.replaceAll("/+", "/");
            }
            final String encoded = URLEncoder.encode(value, DEFAULT_ENCODING);

            final Matcher matcher = ENCODED_CHARACTERS_PATTERN.matcher(encoded);
            final StringBuffer buffer = new StringBuffer(encoded.length());

            while (matcher.find()) {
                String replacement = matcher.group(0);

                if ("+".equals(replacement)) {
                    replacement = "%20";
                } else if ("*".equals(replacement)) {
                    replacement = "%2A";
                } else if ("%7E".equals(replacement)) {
                    replacement = "~";
                } else if (path && "%2F".equals(replacement)) {
                    replacement = "/";
                }

                matcher.appendReplacement(buffer, replacement);
            }

            matcher.appendTail(buffer);
            return buffer.toString();

        } catch (UnsupportedEncodingException ex) {
            throw new AmazonPayClientException("Encountered UnsupportedEncodingException:", ex);
        }
    }

    /**
     * Generates a lower case string from the given string
     * @param str the string to be converted
     * @return the lower case string
     */
    public static String lowerCase(final String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return str.toLowerCase(Locale.ENGLISH);
    }

    /**
     * Generates the current time stamp in "yyyyMMdd'T'HHmmss'Z'" format
     * @return the formatted timestamp
     */
    public static String getFormattedTimestamp() {
        final Date now = new Date();
        final SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
                "yyyyMMdd'T'HHmmss'Z'");
        dateTimeFormat.setTimeZone(new SimpleTimeZone(0, "UTC"));
        return dateTimeFormat.format(now);
    }

    /**
     * Builds the PrivateKey object fromt the private key string provided
     * @param privateKeyString the private key string provided
     * @return the PrivateKey object
     * @throws AmazonPayClientException
     */
    public static PrivateKey buildPrivateKeyFromString(final String privateKeyString) throws AmazonPayClientException {
        Security.addProvider(new BouncyCastleProvider());

        if (privateKeyString == null || privateKeyString.isEmpty()) {
            throw new AmazonPayClientException("Private key string cannot be null or empty");
        }

        PemObject pemObject = getPEMObjectFromKey(privateKeyString);

        if (pemObject == null) {
            throw new AmazonPayClientException("Private key string provided is not valid");
        }

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pemObject.getContent());

        PrivateKey privateKey = null;
        try {
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AmazonPayClientException(e.getMessage(), e);
        }

        return privateKey;
    }

    /**
     * To read the contents of the private key
     * @param privateKey the private key string
     * @return private key pem object
     * @throws AmazonPayClientException
     */
    private static PemObject getPEMObjectFromKey(final String privateKey) throws AmazonPayClientException {
        PemObject pemObject;
        try {
            final PemReader pemReader = new PemReader(new StringReader(privateKey));
            pemObject = pemReader.readPemObject();
            pemReader.close();

        } catch (IOException e) {
            throw new AmazonPayClientException(e.getMessage(), e);
        }

        return pemObject;
    }

    /**
     * To get the service URI
     * @param payConfiguration the PayConfiguration object
     * @param action the action to be performed by the request
     * @return the service URI
     * @throws AmazonPayClientException
     */
    public static URI getServiceURI(PayConfiguration payConfiguration, String action) throws AmazonPayClientException {
        URI uri;
        try {
            uri = new URI(ServiceConstants.endpointMappings.get(payConfiguration.getRegion())
                    + getServiceVersionName(payConfiguration.getEnvironment(), action));
        } catch (URISyntaxException e) {
            throw new AmazonPayClientException(e.getMessage(), e);
        }
        return uri;
    }

    /**
     * To get the service version name
     * @param environment the environment (Sandbox/Live)
     * @param action the action to be performed by the request
     * @return the service version name
     */
    private static String getServiceVersionName(Environment environment, String action) {
        String serviceVersionName;

        if (environment == Environment.SANDBOX) {
            serviceVersionName = "/" + "sandbox" + "/" + action;
        } else {
            serviceVersionName = "/" + "live" + "/" + action;
        }
        return serviceVersionName;
    }

    /**
     * Returns the next wait interval, in milliseconds, using an exponential
     * backoff algorithm.
     * @param retryCount The current retry count
     * @return the wait time
     */
    public static long getExponentialWaitTime(int retryCount) {
        return ((long) Math.pow(2, retryCount) * 1000L);
    }

    /**
     * Returns the header with idempotency key, if not provided by the Merchant.
     * Used for Webstore POST requests.
     * @param header Header sent by the merchant
     * @return header Header with Idempotency key
     */
    public static Map<String, String> updateHeader(Map<String, String> header) {
        if (header == null || header.isEmpty()) {
            final Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));
            return headerMap;
        }

        boolean isIdempotencyKeyPresent = false;
        for (Map.Entry<String, String> entry : header.entrySet()) {
            if (entry.getKey().toLowerCase().equals("x-amz-pay-idempotency-key")) {
                isIdempotencyKeyPresent = true;
                break;
            }
        }

        if (!isIdempotencyKeyPresent) {
            header.put("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));
        }

        return header;
    }
}
