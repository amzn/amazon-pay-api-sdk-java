package com.amazon.pay.api;

/**
 * Implement an Exponential Backoff Retry Strategy
 */
public class ExponentialRetryStrategy implements RetryStrategy {
    /**
     * The delay to apply to the initial retry.  Affects the delay of all subsequent retries.
     */
    protected final long initialDelayMillis;

    /**
     * The delay to apply to the initial retry if the last attempt was throttled.
     * Affects the delay of all subsequent throttling retries.
     */
    protected final long initialThrottlingDelayMillis;

    /**
     * The maximum delay to apply to a non-throttled retry
     */
    protected final long maxDelayMillis;

    /**
     * The maximum delay to apply when retrying a throttled request.
     */
    protected final long maxThrottlingDelayMillis;

    /**
     * The maximum number of retry attempts.
     */
    protected int maxRetries;

    /**
     * Instantiate Exponential backoff retry strategy with the same delay for throttled and non-throttled retries
     * and no maximum delay for throttled or non-throttled retries.
     * @param maxRetries The maximum number of retries
     * @param initialDelayMillis The delay constant to apply to all retries
     */
    public ExponentialRetryStrategy(int maxRetries, long initialDelayMillis) {
        this(maxRetries, initialDelayMillis, initialDelayMillis);
    }

    /**
     * Instantiate Exponential backoff retry strategy with no maximum delay for throttled or non-throttled retries.
     * @param maxRetries The maximum number of retries
     * @param initialDelayMillis The delay constant to apply to non-throttled retries
     * @param initialThrottlingDelayMillis The delay constant to apply to throttled retries
     */
    public ExponentialRetryStrategy(int maxRetries, long initialDelayMillis, long initialThrottlingDelayMillis) {
        this(maxRetries, initialDelayMillis, initialThrottlingDelayMillis, Long.MAX_VALUE, Long.MAX_VALUE);
    }

    /**
     * Instantiate Exponential backoff retry strategy
     * @param maxRetries The maximum number of retries
     * @param initialDelayMillis The delay constant to apply to non-throttled retries
     * @param initialThrottlingDelayMillis The delay constant to apply to throttled retries
     * @param maxDelayMillis The maximum delay to apply to a non-throttled retry
     * @param maxThrottlingDelayMillis The maximum delay to apply when retrying a throttled request.
     */
    public ExponentialRetryStrategy(int maxRetries, long initialDelayMillis, long initialThrottlingDelayMillis,
                                    long maxDelayMillis, long maxThrottlingDelayMillis) {
        this.maxRetries = maxRetries;
        this.initialDelayMillis = initialDelayMillis;
        this.initialThrottlingDelayMillis = initialThrottlingDelayMillis;
        this.maxDelayMillis = maxDelayMillis;
        this.maxThrottlingDelayMillis = maxThrottlingDelayMillis;
    }

    @Override
    public long getWaitTime(int retryCount, int statusCode) {
        if(isThrottledResponse(statusCode)) {
            return Math.min(getExponentialWaitTime(retryCount, initialThrottlingDelayMillis),
                    maxThrottlingDelayMillis);
        } else {
            return Math.min(getExponentialWaitTime(retryCount, initialDelayMillis), maxDelayMillis);
        }
    }

    @Override
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * Determine if a response indicates a throttled request
     * @param statusCode The status code returned by the last request
     * @return True if the last request was throttled, otherwise false
     */
    public boolean isThrottledResponse(int statusCode) {
        return (statusCode == 429);
    }

    /**
     * Returns the next wait interval, in milliseconds, using an exponential
     * backoff algorithm.
     * @param retryCount The current retry count
     * @param initialDelayMillis The constant multiplier for the exponential
     * @return the wait time
     */
    long getExponentialWaitTime(int retryCount, long initialDelayMillis) {
        return ((long) Math.pow(2, retryCount) * initialDelayMillis);
    }
}
