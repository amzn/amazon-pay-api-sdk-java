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
import com.amazon.pay.api.types.AmazonSignatureAlgorithm;

import java.security.PrivateKey;
import java.util.Objects;

public class PayConfiguration {
    private Region region;
    private String publicKeyId;
    private PrivateKey privateKey;
    private Environment environment;
    private AmazonSignatureAlgorithm algorithm;
    private boolean userAgentRedaction = false;
    private ProxySettings proxySettings;
    protected String overrideServiceURL;
    private int clientConnections;
    private RetryStrategy retryStrategy = new DefaultRetryStrategy();
    /** customize request config */
    private RequestConfig requestConfig;
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
     * @deprecated This method is deprecated, instead use setPrivateKey(char[] privateKey)
     * @param privateKey The private key String
     * @return the PayConfiguration object
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    @Deprecated
    public PayConfiguration setPrivateKey(final String privateKey) throws AmazonPayClientException {
        return setPrivateKey(Util.buildPrivateKey(privateKey.toCharArray()));
    }

    /**
     * @param privateKey The private key char array
     * @return the PayConfiguration object
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public PayConfiguration setPrivateKey(final char[] privateKey) throws AmazonPayClientException {
        return setPrivateKey(Util.buildPrivateKey(privateKey));
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
     * @return returns Algorithm from PayConfiguration, default algorithm is AMZN-PAY-RSASSA-PSS if not set
     */
    public AmazonSignatureAlgorithm getAlgorithm() {
        if(algorithm != null) {
            return algorithm;
        } else {
            return AmazonSignatureAlgorithm.DEFAULT;
        }
    }

    /**
     * @param algorithm the Amazon Signature Algorithm
     * @return the PayConfiguration object
     */
    public PayConfiguration setAlgorithm(final String algorithm) {
        this.algorithm = AmazonSignatureAlgorithm.returnIfValidAlgorithm(algorithm);
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
        return retryStrategy.getMaxRetries();
    }

    /**
     * @param maxRetries Sets the maximum number of retries to be made in case of internal server
     *                   errors or throttling errors, in PayConfiguration
     * @return the PayConfiguration object
     */
    public PayConfiguration setMaxRetries(final int maxRetries) {
        retryStrategy.setMaxRetries(maxRetries);
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
     * @return proxySettings Returns Proxy Settings in PayConfiguration
     */
    public ProxySettings getProxySettings() {
        return proxySettings;
    }

    /**
     * @param proxySettings Sets the Proxy Settings in PayConfiguration
     * This should only be used if you need to enable internet traffic flows through the proxy server
     * @return the PayConfiguration object
     */
    public PayConfiguration setProxySettings(ProxySettings proxySettings) {
        this.proxySettings = proxySettings;
        return this;
    }
    
    /**
     * @return overrideServiceURL Returns overridden MWS Service URL in PayConfiguration
     */
    public String getOverrideServiceURL() {
        return overrideServiceURL;
    }

    /**
     * @return Returns clientConnections from PayConfiguration
     */
    public int getClientConnections() {
        if( clientConnections != 0 ) {
            return clientConnections;
        } else {
            return ServiceConstants.MAX_CLIENT_CONNECTIONS;
        }
    }

    /**
     * @param clientConnections Sets the maximum number of Client Connections to be made
     * @return the PayConfiguration object
     */
    public PayConfiguration setClientConnections(int clientConnections) {
        this.clientConnections = clientConnections;
        return this;
    }

    /**
     * Get the retry strategy
     * @return Retry strategy
     */
    public RetryStrategy getRetryStrategy() {
        return retryStrategy;
    }

    /**
     * Set the retry strategy
     * @param retryStrategy The retry strategy to use
     * @return the PayConfiguration object
     */
    public PayConfiguration setRetryStrategy(RetryStrategy retryStrategy) {
        Objects.requireNonNull(retryStrategy, "retryStrategy must not be null");
        this.retryStrategy = retryStrategy;
        return this;
    }

    /**
     * Set request config
     * @param requestConfig
     * @return the PayConfiguration object
     */
    public PayConfiguration setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
        return this;
    }

    /**
     * Get request config
     * @return request config
     */
    public RequestConfig getRequestConfig() {
        return this.requestConfig;
    }
}
