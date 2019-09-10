/**
 * Copyright 2018-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazon.pay.api;

import com.amazon.pay.api.exceptions.AmazonPayClientException;
import com.amazon.pay.api.PayConfiguration;
import com.amazon.pay.api.types.Environment;
import com.amazon.pay.api.types.Region;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public class InstoreClient extends AmazonPayClient {
    final private PayConfiguration payConfiguration;
    final private RequestSigner requestSigner;
    final private Map<String, List<String>> queryParametersMap = new HashMap<>();

    public InstoreClient(final PayConfiguration payConfiguration) throws AmazonPayClientException {
        super(payConfiguration);
        this.payConfiguration = payConfiguration;
        requestSigner = new RequestSigner(payConfiguration);
    }

    /**
     * Helps the solution provider make the merchantScan request with their auth token
     * @param scanRequest is the scan request body
     * @param header consists the authToken of the merchant
     * @return the merchant scan response
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse merchantScan(final JSONObject scanRequest, final Map<String, String> header) throws AmazonPayClientException {
        final URI scanURI = Util.getServiceURI(payConfiguration, ServiceConstants.MERCHANT_SCAN);
        return callAPI(scanURI, "POST", queryParametersMap, scanRequest.toString(), header);
    }

    /**
     * Helps the merchant make the merchantScan request
     * @param scanRequest is the scan request body
     * @return the merchant scan response
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse merchantScan(final JSONObject scanRequest) throws AmazonPayClientException {
        return merchantScan(scanRequest, null);
    }

    /**
     * Helps the solution provider make the charge request with their auth token
     * @param chargeRequest is the charge request body
     * @param header consists the authToken of the merchant
     * @return the charge response
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse charge(final JSONObject chargeRequest, final Map<String, String> header) throws AmazonPayClientException {
        final URI chargeURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHARGE);
        return callAPI(chargeURI, "POST", queryParametersMap, chargeRequest.toString(), header);
    }

    /**
     * Helps the merchant make the charge request
     * @param chargeRequest is the charge request body
     * @return the charge response
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse charge(final JSONObject chargeRequest) throws AmazonPayClientException {
        return charge(chargeRequest, null);
    }

    /**
     * Helps the solution provider make the refund request with their auth token
     * @param refundRequest is the refund request body
     * @param header consists the authToken of the merchant
     * @return the refund response
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse refund(final JSONObject refundRequest, final Map<String, String> header) throws AmazonPayClientException {
        final URI refundURI = Util.getServiceURI(payConfiguration, ServiceConstants.REFUND);
        return callAPI(refundURI, "POST", queryParametersMap, refundRequest.toString(), header);
    }

    /**
     * Helps the merchant make the refund request
     * @param refundRequest is the refund request body
     * @return the refund response
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse refund(final JSONObject refundRequest) throws AmazonPayClientException {
        return refund(refundRequest, null);
    }
}