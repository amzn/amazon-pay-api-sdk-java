package com.amazon.pay.api;

/**
 * The default retry strategy retries up to 3 times, using an exponential backoff strategy of 2000 * 2^retry
 * regardless of the request failure reason.
 */
public class DefaultRetryStrategy extends ExponentialRetryStrategy {
    /**
     * Default to 3 retries
     */
    private static final int DEFAULT_MAX_RETRIES = 3;

    /**
     * Delays between retries are 2s, 4s, 8s
     */
    private static final long DEFAULT_INITIAL_DELAY = 2000L;

    public DefaultRetryStrategy() {
        super(DEFAULT_MAX_RETRIES, DEFAULT_INITIAL_DELAY);
    }
}
