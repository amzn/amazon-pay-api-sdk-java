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

public class ProxySettings {
    private String proxyHost;
    private Integer proxyPort;
    private String proxyUser;
    private char[] proxyPassword;

    /**
     * @return Returns proxyHost from ProxySettings
     */
    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * @param proxyHost The Proxy Host
     * @return the ProxySettings object
     */
    public ProxySettings setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        return this;
    }

    /**
     * @return Returns proxyPort from ProxySettings
     */
    public Integer getProxyPort() {
        return proxyPort;
    }

    /**
     * @param proxyHost The Proxy Port
     * @return the ProxySettings object
     */
    public ProxySettings setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
        return this;
    }

    /**
     * @return Returns proxyUser from ProxySettings
     */
    public String getProxyUser() {
        return proxyUser;
    }

    /**
     * @param proxyHost The Proxy User
     * @return the ProxySettings object
     */
    public ProxySettings setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
        return this;
    }

    /**
     * @return Returns proxyPassword from ProxySettings
     */
    public char[] getProxyPassword() {
        return proxyPassword;
    }

    /**
     * @param proxyHost The Proxy Password
     * @return the ProxySettings object
     */
    public ProxySettings setProxyPassword(char[] proxyPassword) {
        this.proxyPassword = proxyPassword;
        return this;
    }

}
