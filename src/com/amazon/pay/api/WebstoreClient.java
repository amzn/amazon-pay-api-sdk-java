/**
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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


public class WebstoreClient extends AmazonPayClient {

    public WebstoreClient(final PayConfiguration payConfiguration) throws AmazonPayClientException {
        super(payConfiguration);
    }

    /**
     * The CreateCheckoutSession operation is used to create a CheckoutSession for a buyer
     * and pass the Id as part of button click.
     *
     * @param payload
     * @param header
     * @return The response from the CreateCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse createCheckoutSession(final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI createCheckoutSessionURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHECKOUT_SESSIONS);
        final Map<String, String> headerMap = Util.updateHeader(header);
        return callAPI(createCheckoutSessionURI, "POST", queryParametersMap, payload.toString(), headerMap);
    }

    /**
     * The CreateCheckoutSession operation is used to create a CheckoutSession for a buyer
     * and pass the Id as part of button click.
     *
     * @param payload
     * @return The response from the CreateCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse createCheckoutSession(final JSONObject payload) throws AmazonPayClientException {
        return createCheckoutSession(payload, null);
    }

    /**
     * The GetCheckoutSession operation is used to get checkout session details that contain
     * all session associated details.
     *
     * @param checkoutSessionId
     * @param header
     * @return The response from the GetCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse getCheckoutSession(final String checkoutSessionId, final Map<String, String> header) throws AmazonPayClientException {
        final URI checkoutSessionURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHECKOUT_SESSIONS);
        final URI getCheckoutSessionURI = checkoutSessionURI.resolve(checkoutSessionURI.getPath() + "/" + checkoutSessionId);
        return callAPI(getCheckoutSessionURI, "GET", queryParametersMap, "", header);
    }

    /**
     * The GetCheckoutSession operation is used to get checkout session details that contain
     * all session associated details.
     *
     * @param checkoutSessionId
     * @return The response from the GetCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse getCheckoutSession(final String checkoutSessionId) throws AmazonPayClientException {
        return getCheckoutSession(checkoutSessionId, null);
    }

    /**
     * The UpdateCheckoutSession operation is used to update payment details for a session.
     *
     * @param checkoutSessionId
     * @param payload
     * @param header
     * @return The response from the UpdateCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse updateCheckoutSession(final String checkoutSessionId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI checkoutSessionURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHECKOUT_SESSIONS);
        final URI updateCheckoutSessionURI = checkoutSessionURI.resolve(checkoutSessionURI.getPath() + "/" + checkoutSessionId);
        return callAPI(updateCheckoutSessionURI, "PATCH", queryParametersMap, payload.toString(), header);
    }

    /**
     * The UpdateCheckoutSession operation is used to update payment details for a session.
     *
     * @param checkoutSessionId
     * @param payload
     * @return The response from the UpdateCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse updateCheckoutSession(final String checkoutSessionId, final JSONObject payload) throws AmazonPayClientException {
        return updateCheckoutSession(checkoutSessionId, payload, null);
    }

    /**
     * The CompleteCheckoutSession operation is used to confirm completion of a checkout session
     *
     * @param checkoutSessionId
     * @param payload
     * @param header
     * @return The response from the CompleteCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse completeCheckoutSession(final String checkoutSessionId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI checkoutSessionURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHECKOUT_SESSIONS);
        final URI completeCheckoutSessionURI = checkoutSessionURI.resolve(checkoutSessionURI.getPath() + "/" + checkoutSessionId + "/" + "complete");
        return callAPI(completeCheckoutSessionURI, "POST", queryParametersMap, payload.toString(), header);
    }

    /**
     * The CompleteCheckoutSession operation is used to confirm completion of a checkout session.
     *
     * @param checkoutSessionId
     * @param payload
     * @return The response from the CompleteCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse completeCheckoutSession(final String checkoutSessionId, final JSONObject payload) throws AmazonPayClientException {
        return completeCheckoutSession(checkoutSessionId, payload, null);
    }

    /**
     * The GetChargePermission operation is used to get the complete details of ChargePermission.
     *
     * @param chargePermissionsId
     * @param header
     * @return The response from the GetChargePermission service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse getChargePermission(final String chargePermissionsId, final Map<String, String> header) throws AmazonPayClientException {
        final URI chargePermissionURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHARGE_PERMISSIONS);
        final URI getChargePermissionURI = chargePermissionURI.resolve(chargePermissionURI.getPath() + "/" + chargePermissionsId);
        return callAPI(getChargePermissionURI, "GET", queryParametersMap, "", header);
    }

    /**
     * The GetChargePermission operation is used to get the complete details of ChargePermission.
     *
     * @param chargePermissionsId
     * @return The response from the GetChargePermission service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse getChargePermission(final String chargePermissionsId) throws AmazonPayClientException {
        return getChargePermission(chargePermissionsId, null);
    }

    /**
     * The UpdateChargePermission operation is used to update the metadata of the ChargePermission.
     *
     * @param chargePermissionsId
     * @param payload
     * @param header
     * @return The response from the UpdateChargePermission service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse updateChargePermission(final String chargePermissionsId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI chargePermissionURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHARGE_PERMISSIONS);
        final URI updateChargePermissionURI = chargePermissionURI.resolve(chargePermissionURI.getPath() + "/" + chargePermissionsId);
        return callAPI(updateChargePermissionURI, "PATCH", queryParametersMap, payload.toString(), header);
    }

    /**
     * The UpdateChargePermission operation is used to update the metadata of the ChargePermission.
     *
     * @param chargePermissionsId
     * @param payload
     * @return The response from the UpdateChargePermission service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse updateChargePermission(final String chargePermissionsId, final JSONObject payload) throws AmazonPayClientException {
        return updateChargePermission(chargePermissionsId, payload, null);
    }

    /**
     * The CloseChargePermission operation Moves the Charge Permission to a Closed state.
     * No future charges can be made and pending charges will be canceled if you set cancelPendingCharges to true.
     *
     * @param chargePermissionsId
     * @param payload
     * @param header
     * @return The response from the CloseChargePermission service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse closeChargePermission(final String chargePermissionsId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI chargePermissionURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHARGE_PERMISSIONS);
        final URI closeChargePermissionURI = chargePermissionURI.resolve(chargePermissionURI.getPath() + "/" + chargePermissionsId + "/" + "close");
        return callAPI(closeChargePermissionURI, "DELETE", queryParametersMap, payload.toString(), header);
    }

    /**
     * The CloseChargePermission operation Moves the Charge Permission to a Closed state.
     * No future charges can be made and pending charges will be canceled if you set cancelPendingCharges to true.
     *
     * @param chargePermissionsId
     * @param payload
     * @return The response from the CloseChargePermission service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse closeChargePermission(final String chargePermissionsId, final JSONObject payload) throws AmazonPayClientException {
        return closeChargePermission(chargePermissionsId, payload, null);
    }

    /**
     * The CreateCharge operation is used to create a charges for a buyer
     * and pass the Id as part of button click.
     *
     * @param payload
     * @param header
     * @return The response from the CreateCharge service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse createCharge(final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI createChargesURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHARGES);
        final Map<String, String> headerMap = Util.updateHeader(header);
        return callAPI(createChargesURI, "POST", queryParametersMap, payload.toString(), headerMap);
    }

    /**
     * The CreateCharge operation is used to create a charges for a buyer
     * and pass the Id as part of button click.
     *
     * @param payload
     * @return The response from the CreateCharge service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse createCharge(final JSONObject payload) throws AmazonPayClientException {
        return createCharge(payload, null);
    }

    /**
     * The getCharge operation is used to get charges details that contain
     *
     * @param chargeId
     * @param header
     * @return The response from the getCharge service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse getCharge(final String chargeId, final Map<String, String> header) throws AmazonPayClientException {
        final URI chargesURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHARGES);
        final URI getChargeURI = chargesURI.resolve(chargesURI.getPath() + "/" + chargeId);
        return callAPI(getChargeURI, "GET", queryParametersMap, "", header);
    }

    /**
     * The getCharge operation is used to get charges details that contain
     *
     * @param chargeId
     * @return The response from the getCharge service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse getCharge(final String chargeId) throws AmazonPayClientException {
        return getCharge(chargeId, null);
    }

    /**
     * The CaptureCharge operation is used to create a charges for a buyer
     * and pass the Id as part of button click.
     *
     * @param chargeId
     * @param payload
     * @param header
     * @return The response from the CaptureCharge service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse captureCharge(final String chargeId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI chargesURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHARGES);
        final URI captureChargeURI = chargesURI.resolve(chargesURI.getPath() + "/" + chargeId + "/" + "capture");
        final Map<String, String> headerMap = Util.updateHeader(header);
        return callAPI(captureChargeURI, "POST", queryParametersMap, payload.toString(), headerMap);
    }

    /**
     * The CaptureCharge operation is used to create a charges for a buyer
     * and pass the Id as part of button click.
     *
     * @param chargeId
     * @param payload
     * @return The response from the CaptureCharge service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse captureCharge(final String chargeId, final JSONObject payload) throws AmazonPayClientException {
        return captureCharge(chargeId, payload, null);
    }

    /**
     * The cancelCharge operation is used to cancel Charges.
     *
     * @param chargeId
     * @param payload
     * @param header
     * @return The response from the cancelCharge service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse cancelCharge(final String chargeId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI chargesURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHARGES);
        final URI cancelChargeURI = chargesURI.resolve(chargesURI.getPath() + "/" + chargeId + "/" + "cancel");
        return callAPI(cancelChargeURI, "DELETE", queryParametersMap, payload.toString(), header);
    }

    /**
     * The cancelCharge operation is used to cancel Charges.
     *
     * @param chargeId
     * @param payload
     * @return The response from the cancelCharge service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse cancelCharge(final String chargeId, final JSONObject payload) throws AmazonPayClientException {
        return cancelCharge(chargeId, payload, null);
    }

    /**
     * The CreateRefund operation is used to create a refund for a buyer
     * and pass the Id as part of button click.
     *
     * @param payload
     * @param header
     * @return The response from the CreateRefunds service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse createRefund(final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI createRefundsURI = Util.getServiceURI(payConfiguration, ServiceConstants.REFUNDS);
        final Map<String, String> headerMap = Util.updateHeader(header);
        return callAPI(createRefundsURI, "POST", queryParametersMap, payload.toString(), headerMap);
    }

    /**
     * The CreateRefund operation is used to create a refund for a buyer
     * and pass the Id as part of button click.
     *
     * @param payload
     * @return The response from the CreateRefunds service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse createRefund(final JSONObject payload) throws AmazonPayClientException {
        return createRefund(payload, null);
    }

    /**
     * The getRefund operation is used to get refund details that contain
     *
     * @param refundId
     * @param header
     * @return The response from the getRefund service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse getRefund(final String refundId, final Map<String, String> header) throws AmazonPayClientException {
        final URI refundsURI = Util.getServiceURI(payConfiguration, ServiceConstants.REFUNDS);
        final URI getRefundURI = refundsURI.resolve(refundsURI.getPath() + "/" + refundId + "/");
        return callAPI(getRefundURI, "GET", queryParametersMap, "", header);
    }

    /**
     * The getRefund operation is used to get refund details that contain
     *
     * @param refundId
     * @return The response from the getRefund service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException
     */
    public AmazonPayResponse getRefund(final String refundId) throws AmazonPayClientException {
        return getRefund(refundId, null);
    }
}