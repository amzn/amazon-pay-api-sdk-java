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
import com.amazon.pay.api.types.Environment;
import com.amazon.pay.api.types.Region;

import java.security.PrivateKey;

public class PayConfiguration {
    private Region region;
    private String publicKeyId;
    private PrivateKey privateKey;
    private Environment environment;
    private int maxRetries = 3;
    private boolean userAgentRedaction = false;
    private String overrideServiceURL;

    /**
     * @return Returns region code from PayConfiguration
     */
    public Region getRegion() {
        return region;
    }

    /**
     * @param region Identifies region associated with Amazon Pay API operations.
     * @return the PayConfiguration object
     */
    public PayConfiguration setRegion(final Region region) {
        this.region = region;
        return this;
    }

    /**
     * @return returns the public key id from the PayConfiguration
     */
    public String getPublicKeyId() {
        return publicKeyId;
    }

    /**
     * @param publicKeyId The public key id of the merchant
     * @return the PayConfiguration object
     */
    public PayConfiguration setPublicKeyId(final String publicKeyId) {
        this.publicKeyId = publicKeyId;
        return this;
    }

    /**
     * @return returns the private key object from the PayConfiguration
     */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * @param privateKey The private key string
     * @return the PayConfiguration object
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public PayConfiguration setPrivateKey(final String privateKey) throws AmazonPayClientException {
        return setPrivateKey(Util.buildPrivateKeyFromString(privateKey));
    }

    /**
     * @param privateKey the PrivateKey object
     * @return the PayConfiguration object
     */
    public PayConfiguration setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    /**
     * @return returns the environment from the PayConfiguration
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * @param environment the environment,i.e, Sandbox or Live
     * @return the PayConfiguration object
     */
    public PayConfiguration setEnvironment(Environment environment) {
        this.environment = environment;
        return this;
    }

    /**
     * @return the maximum number of retries to be made
     */
    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * @param maxRetries Sets the maximum number of retries to be made in case of internal server
     *                   errors or throttling errors, in PayConfiguration
     * @return the PayConfiguration object
     */
    public PayConfiguration setMaxRetries(final int maxRetries) {
        this.maxRetries = maxRetries;
        return this;
    }

    /**
     * Returns true if the merchant wants to set the Java and OS version segment in
     * the User-Agent header to 'Redacted'.
     *
     * @return boolean userAgentRedaction
     */
    public boolean isUserAgentRedaction() {
        return userAgentRedaction;
    }


    /**
     * Sets userAgentRedaction in PayConfiguration
     * If this flag is set to true, the Java and OS version segment in the User-Agent header is
     * 'Redacted'.
     *
     * @param userAgentRedaction - argument that sets userAgentRedaction in PayConfiguration
     * @return the PayConfiguration object
     */
    public PayConfiguration setUserAgentRedaction(final boolean userAgentRedaction) {
        this.userAgentRedaction = userAgentRedaction;
        return this;
    }

    /**
     * @return overrideServiceURL Returns overridden MWS Service URL in PayConfiguration
     */
    public String getOverrideServiceURL() {
        return overrideServiceURL;
    }

    /**
     * @param overrideServiceURL Sets CV2 Service URL override in PayConfiguration
     * This should only be used if you need to programmatically override the default service endpoint
     * provided by the SDK's endpointMappingsMap in com.amazon.pay.types.ServiceConstants.
     */
    public PayConfiguration setOverrideServiceURL(String overrideServiceURL) {
        this.overrideServiceURL = overrideServiceURL;
        return this;
    }

}
