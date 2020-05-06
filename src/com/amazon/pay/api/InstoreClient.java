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
import net.sf.json.JSONObject;

import java.net.URI;
import java.util.Map;

public class InstoreClient extends AmazonPayClient {

    public InstoreClient(final PayConfiguration payConfiguration) throws AmazonPayClientException {
        super(payConfiguration);
    }

    /**
     * Helps the solution provider make the merchantScan request with their auth token
     * @param scanRequest is the scan request body
     * @param header consists the authToken of the merchant
     * @return the merchant scan response
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse merchantScan(final JSONObject scanRequest, final Map<String, String> header) throws AmazonPayClientException {
        final URI scanURI = Util.getServiceURI(payConfiguration, ServiceConstants.INSTORE_MERCHANT_SCAN);
        return callAPI(scanURI, "POST", null, scanRequest.toString(), header);
    }

    /**
     * Helps the merchant make the merchantScan request
     * @param scanRequest is the scan request body
     * @return the merchant scan response
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse merchantScan(final JSONObject scanRequest) throws AmazonPayClientException {
        return merchantScan(scanRequest, null);
    }

    /**
     * Helps the solution provider make the charge request with their auth token
     * @param chargeRequest is the charge request body
     * @param header consists the authToken of the merchant
     * @return the charge response
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse charge(final JSONObject chargeRequest, final Map<String, String> header) throws AmazonPayClientException {
        final URI chargeURI = Util.getServiceURI(payConfiguration, ServiceConstants.INSTORE_CHARGE);
        return callAPI(chargeURI, "POST", null, chargeRequest.toString(), header);
    }

    /**
     * Helps the merchant make the charge request
     * @param chargeRequest is the charge request body
     * @return the charge response
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse charge(final JSONObject chargeRequest) throws AmazonPayClientException {
        return charge(chargeRequest, null);
    }

    /**
     * Helps the solution provider make the refund request with their auth token
     * @param refundRequest is the refund request body
     * @param header consists the authToken of the merchant
     * @return the refund response
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse refund(final JSONObject refundRequest, final Map<String, String> header) throws AmazonPayClientException {
        final URI refundURI = Util.getServiceURI(payConfiguration, ServiceConstants.INSTORE_REFUND);
        return callAPI(refundURI, "POST", null, refundRequest.toString(), header);
    }

    /**
     * Helps the merchant make the refund request
     * @param refundRequest is the refund request body
     * @return the refund response
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse refund(final JSONObject refundRequest) throws AmazonPayClientException {
        return refund(refundRequest, null);
    }
}
