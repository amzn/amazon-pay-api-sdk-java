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

public final class ConnectionPoolStats {

    private final int maxConnections;
    private final int availableConnections;
    private final int pendingConnections;
    private final int leasedConnections;

    public ConnectionPoolStats(int maxConnections, int availableConnections, int pendingConnections, int leasedConnections) {
        this.maxConnections = maxConnections;
        this.availableConnections = availableConnections;
        this.pendingConnections = pendingConnections;
        this.leasedConnections = leasedConnections;
    }

    /**
     *
     * @return the maximum number of allowed persistent connections.
     */
    public int getMaxConnections() {
        return maxConnections;
    }

    /**
     * the number idle persistent connections in the connection pool.
     * @return
     */
    public int getAvailableConnections() {
        return availableConnections;
    }

    /**
     *
     * @return the number of connection requests being blocked, awaiting for a free connection.
     */
    public int getPendingConnections() {
        return pendingConnections;
    }

    /**
     *
     * @return the number of persistent connections tracked by the connection manager currently being used to execute requests.
     */
    public int getLeasedConnections() {
        return leasedConnections;
    }

    /**
     * @returns the connection pool details for the caller, to monitor the connection pool performance.
     */
    @Override
    public String toString() {
        return "ConnectionPoolStats{"
                + "Total=" + maxConnections
                + ", Available=" + availableConnections
                + ", Pending=" + pendingConnections
                + ", Leased=" + leasedConnections + "}";
    }
}
