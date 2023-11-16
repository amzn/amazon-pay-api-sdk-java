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

/** Allows customizing apache client RequestConfig */
public class RequestConfig {
    /** the time waiting for requesting a connection from the connection manager */
    private final int connectionTimeoutMillis;
    /** the time to establish the connection with the remote host */
    private final int connectTimeoutMillis;
    /** the time waiting for data – after establishing the connection; read data timeout */
    private final int socketTimeoutMillis;

    public RequestConfig(int connectionTimeoutMillis, int connectTimeoutMillis, int socketTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.connectionTimeoutMillis = connectionTimeoutMillis;
        this.socketTimeoutMillis = socketTimeoutMillis;
    }

    /**
     * Get connection timeout in milliseconds
     * @return time in millis to wait for a connection from connection manager
     */
    public int getConnectionTimeoutMillis() {
        return this.connectionTimeoutMillis;
    }

    /**
     * Get connect timeout in milliseconds
     * @return time in milliseconds to wait for establishing a connection with server
     */
    public int getConnectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }

    /**
     * Get socket timeout in milliseconds
     * @return time in milliseconds to wait for reading data from the connection.
     */
    public int getSocketTimeoutMillis() {
        return this.socketTimeoutMillis;
    }
}
