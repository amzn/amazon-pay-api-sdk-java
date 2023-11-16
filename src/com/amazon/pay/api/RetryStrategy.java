package com.amazon.pay.api;

/**
 * Interface defining the retry strategy to apply when a request returns a service error
 */
public interface RetryStrategy {

    /**
     * Return how long to wait until the next request
     * @param retryCount The number of retries that have been attempted
     * @param statusCode The status code returned by the last attempt
     * @return Milliseconds to wait before the next attempt
     */
    long getWaitTime(int retryCount, int statusCode);

    /**
     * Set the maximum retry attempts.
     * @param maxRetries The number of times to retry a failed request
     */
    void setMaxRetries(int maxRetries);

    /**
     * Get the maximum retry attempts
     * @return The number of times to retry a failed request.
     */
    int getMaxRetries();
}
