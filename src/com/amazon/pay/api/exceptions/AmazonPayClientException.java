package com.amazon.pay.api.exceptions;

import java.io.Serializable;

public class AmazonPayClientException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs AmazonPayClientException with given message and underlying exception
     *
     * @param message   An error message describing the error
     * @param exception Original underlying exception
     */
    public AmazonPayClientException(String message, Exception exception) {
        super(message, exception);
    }

    /**
     * Constructs AmazonPayClientException with given message
     *
     * @param message An error message describing the error
     */
    public AmazonPayClientException(String message) {
        super(message);
    }
}
