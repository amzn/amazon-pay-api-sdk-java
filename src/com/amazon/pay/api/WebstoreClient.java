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
import org.json.JSONObject;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

public class WebstoreClient extends AmazonPayClient {

    public WebstoreClient(final PayConfiguration payConfiguration) throws AmazonPayClientException {
        super(payConfiguration);
    }

    /**
     * Get Buyer details can include buyer ID, name, email address, postal code, and country code
     * when used with the Amazon.Pay.renderButton 'SignIn' productType and corresponding signInScopes.
     *
     * @param buyerToken Buyer Token
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the GetBuyer service API, as returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse getBuyer(final String buyerToken, final Map<String, String> header) throws AmazonPayClientException {
        final URI buyerURI = Util.getServiceURI(payConfiguration, ServiceConstants.BUYERS);
        final URI getBuyerURI = buyerURI.resolve(buyerURI.getPath() + "/" + buyerToken);
        return callAPI(getBuyerURI, "GET", null, "", header);
    }

    /**
     * Get Buyer details can include buyer ID, name, email address, postal code, and country code
     * when used with the Amazon.Pay.renderButton 'SignIn' productType and corresponding signInScopes.
     *
     * @param buyerToken Buyer Token
     * @return The response from the GetBuyer service API, as returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse getBuyer(final String buyerToken) throws AmazonPayClientException {
        return getBuyer(buyerToken, null);
    }

    /**
     * The CreateCheckoutSession operation is used to create a CheckoutSession for a buyer
     * and pass the Id as part of button click.
     *
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the CreateCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse createCheckoutSession(final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI createCheckoutSessionURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHECKOUT_SESSIONS);
        final Map<String, String> headerMap = Util.updateHeader(header);
        return callAPI(createCheckoutSessionURI, "POST", null, payload.toString(), headerMap);
    }

    /**
     * The CreateCheckoutSession operation is used to create a CheckoutSession for a buyer
     * and pass the Id as part of button click.
     *
     * @param payload JSONObject request body
     * @return The response from the CreateCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse createCheckoutSession(final JSONObject payload) throws AmazonPayClientException {
        return createCheckoutSession(payload, null);
    }

    /**
     * The GetCheckoutSession operation is used to get checkout session details that contain
     * all session associated details.
     *
     * @param checkoutSessionId Checkout Session ID provided by Checkout v2 service
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the GetCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse getCheckoutSession(final String checkoutSessionId, final Map<String, String> header) throws AmazonPayClientException {
        final URI checkoutSessionURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHECKOUT_SESSIONS);
        final URI getCheckoutSessionURI = checkoutSessionURI.resolve(checkoutSessionURI.getPath() + "/" + checkoutSessionId);
        final AmazonPayResponse response = callAPI(getCheckoutSessionURI, "GET", null, "", header);
        return Util.enhanceResponseWithShippingAddressList(response);
    }

    /**
     * The GetCheckoutSession operation is used to get checkout session details that contain
     * all session associated details.
     *
     * @param checkoutSessionId Checkout Session ID provided by Checkout v2 service
     * @return The response from the GetCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse getCheckoutSession(final String checkoutSessionId) throws AmazonPayClientException {
        return getCheckoutSession(checkoutSessionId, null);
    }

    /**
     * The UpdateCheckoutSession operation is used to update payment details for a session.
     *
     * @param checkoutSessionId Checkout Session ID provided by Checkout v2 service
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the UpdateCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse updateCheckoutSession(final String checkoutSessionId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI checkoutSessionURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHECKOUT_SESSIONS);
        final URI updateCheckoutSessionURI = checkoutSessionURI.resolve(checkoutSessionURI.getPath() + "/" + checkoutSessionId);
        final AmazonPayResponse response = callAPI(updateCheckoutSessionURI, "PATCH", null, payload.toString(), header);
        return Util.enhanceResponseWithShippingAddressList(response);
    }

    /**
     * The UpdateCheckoutSession operation is used to update payment details for a session.
     *
     * @param checkoutSessionId Checkout Session ID provided by Checkout v2 service
     * @param payload JSONObject request body
     * @return The response from the UpdateCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse updateCheckoutSession(final String checkoutSessionId, final JSONObject payload) throws AmazonPayClientException {
        return updateCheckoutSession(checkoutSessionId, payload, null);
    }

    /**
     * The CompleteCheckoutSession operation is used to confirm completion of a checkout session
     *
     * @param checkoutSessionId Checkout Session ID provided by Checkout v2 service
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the CompleteCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse completeCheckoutSession(final String checkoutSessionId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI checkoutSessionURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHECKOUT_SESSIONS);
        final URI completeCheckoutSessionURI = checkoutSessionURI.resolve(checkoutSessionURI.getPath() + "/" + checkoutSessionId + "/" + "complete");
        final AmazonPayResponse response = callAPI(completeCheckoutSessionURI, "POST", null, payload.toString(), header);
        return Util.enhanceResponseWithShippingAddressList(response);
    }

    /**
     * The CompleteCheckoutSession operation is used to confirm completion of a checkout session.
     *
     * @param checkoutSessionId Checkout Session ID provided by Checkout v2 service
     * @param payload JSONObject request body
     * @return The response from the CompleteCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse completeCheckoutSession(final String checkoutSessionId, final JSONObject payload) throws AmazonPayClientException {
        return completeCheckoutSession(checkoutSessionId, payload, null);
    }

    /**
     * The GetChargePermission operation is used to get the complete details of ChargePermission.
     *
     * @param chargePermissionId Charge Permission ID provided by Checkout v2 service
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the GetChargePermission service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse getChargePermission(final String chargePermissionId, final Map<String, String> header) throws AmazonPayClientException {
        final URI chargePermissionURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHARGE_PERMISSIONS);
        final URI getChargePermissionURI = chargePermissionURI.resolve(chargePermissionURI.getPath() + "/" + chargePermissionId);
        return callAPI(getChargePermissionURI, "GET", null, "", header);
    }

    /**
     * The GetChargePermission operation is used to get the complete details of ChargePermission.
     *
     * @param chargePermissionId Charge Permission ID provided by Checkout v2 service
     * @return The response from the GetChargePermission service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse getChargePermission(final String chargePermissionId) throws AmazonPayClientException {
        return getChargePermission(chargePermissionId, null);
    }

    /**
     * The UpdateChargePermission operation is used to update the metadata of the ChargePermission.
     *
     * @param chargePermissionId Charge Permission ID provided by Checkout v2 service
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the UpdateChargePermission service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse updateChargePermission(final String chargePermissionId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI chargePermissionURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHARGE_PERMISSIONS);
        final URI updateChargePermissionURI = chargePermissionURI.resolve(chargePermissionURI.getPath() + "/" + chargePermissionId);
        return callAPI(updateChargePermissionURI, "PATCH", null, payload.toString(), header);
    }

    /**
     * The UpdateChargePermission operation is used to update the metadata of the ChargePermission.
     *
     * @param chargePermissionId Charge Permission ID provided by Checkout v2 service
     * @param payload JSONObject request body
     * @return The response from the UpdateChargePermission service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse updateChargePermission(final String chargePermissionId, final JSONObject payload) throws AmazonPayClientException {
        return updateChargePermission(chargePermissionId, payload, null);
    }

    /**
     * The CloseChargePermission operation Moves the Charge Permission to a Closed state.
     * No future charges can be made and pending charges will be canceled if you set cancelPendingCharges to true.
     *
     * @param chargePermissionId Charge Permission ID provided by Checkout v2 service
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the CloseChargePermission service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse closeChargePermission(final String chargePermissionId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI chargePermissionURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHARGE_PERMISSIONS);
        final URI closeChargePermissionURI = chargePermissionURI.resolve(chargePermissionURI.getPath() + "/" + chargePermissionId + "/" + "close");
        return callAPI(closeChargePermissionURI, "DELETE", null, payload.toString(), header);
    }

    /**
     * The CloseChargePermission operation Moves the Charge Permission to a Closed state.
     * No future charges can be made and pending charges will be canceled if you set cancelPendingCharges to true.
     *
     * @param chargePermissionId Charge Permission ID provided by Checkout v2 service
     * @param payload JSONObject request body
     * @return The response from the CloseChargePermission service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse closeChargePermission(final String chargePermissionId, final JSONObject payload) throws AmazonPayClientException {
        return closeChargePermission(chargePermissionId, payload, null);
    }

    /**
     * The CreateCharge operation is used to create a charges for a buyer
     * and pass the Id as part of button click.
     *
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the CreateCharge service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse createCharge(final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI createChargesURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHARGES);
        final Map<String, String> headerMap = Util.updateHeader(header);
        return callAPI(createChargesURI, "POST", null, payload.toString(), headerMap);
    }

    /**
     * The CreateCharge operation is used to create a charges for a buyer
     * and pass the Id as part of button click.
     *
     * @param payload JSONObject request body
     * @return The response from the CreateCharge service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse createCharge(final JSONObject payload) throws AmazonPayClientException {
        return createCharge(payload, null);
    }

    /**
     * The getCharge operation is used to get charges details that contain
     *
     * @param chargeId Charge ID provided by Checkout v2 service
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the getCharge service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse getCharge(final String chargeId, final Map<String, String> header) throws AmazonPayClientException {
        final URI chargesURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHARGES);
        final URI getChargeURI = chargesURI.resolve(chargesURI.getPath() + "/" + chargeId);
        return callAPI(getChargeURI, "GET", null, "", header);
    }

    /**
     * The getCharge operation is used to get charges details that contain
     *
     * @param chargeId Charge ID provided by Checkout v2 service
     * @return The response from the getCharge service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse getCharge(final String chargeId) throws AmazonPayClientException {
        return getCharge(chargeId, null);
    }

    /**
     * The updateCharge operation is used to update the charge status of any PSP (Payment Service Provider) processed payment method (PPM) transactions.
     *
     * @param chargeId Charge ID provided by Checkout v2 service.
     * @param payload JSONObject request body.
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken).
     * @return The response from the updateCharge service API, as returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue.
     */
    public AmazonPayResponse updateCharge(final String chargeId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI chargesURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHARGES);
        final URI updateChargeURI = chargesURI.resolve(chargesURI.getPath() + "/" + chargeId);
        final Map<String, String> headerMap = Util.updateHeader(header);
        return callAPI(updateChargeURI, "PATCH", null, payload.toString(), headerMap);
    }

    /**
     * The updateCharge operation is used to update the charge status of any PSP (Payment Service Provider) processed payment method (PPM) transactions.
     *
     * @param chargeId Charge ID provided by Checkout v2 service.
     * @param payload JSONObject request body.
     * @return The response from the updateCharge service API, as returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue.
     */
    public AmazonPayResponse updateCharge(final String chargeId, final JSONObject payload) throws AmazonPayClientException {
        return updateCharge(chargeId, payload, null);
    }

    /**
     * The CaptureCharge operation is used to create a charges for a buyer
     * and pass the Id as part of button click.
     *
     * @param chargeId Charge ID provided by Checkout v2 service
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the CaptureCharge service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse captureCharge(final String chargeId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI chargesURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHARGES);
        final URI captureChargeURI = chargesURI.resolve(chargesURI.getPath() + "/" + chargeId + "/" + "capture");
        final Map<String, String> headerMap = Util.updateHeader(header);
        return callAPI(captureChargeURI, "POST", null, payload.toString(), headerMap);
    }

    /**
     * The CaptureCharge operation is used to create a charges for a buyer
     * and pass the Id as part of button click.
     *
     * @param chargeId Charge ID provided by Checkout v2 service
     * @param payload JSONObject request body
     * @return The response from the CaptureCharge service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse captureCharge(final String chargeId, final JSONObject payload) throws AmazonPayClientException {
        return captureCharge(chargeId, payload, null);
    }

    /**
     * The cancelCharge operation is used to cancel Charges.
     *
     * @param chargeId Charge ID provided by Checkout v2 service
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the cancelCharge service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse cancelCharge(final String chargeId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI chargesURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHARGES);
        final URI cancelChargeURI = chargesURI.resolve(chargesURI.getPath() + "/" + chargeId + "/" + "cancel");
        return callAPI(cancelChargeURI, "DELETE", null, payload.toString(), header);
    }

    /**
     * The cancelCharge operation is used to cancel Charges.
     *
     * @param chargeId Charge ID provided by Checkout v2 service
     * @param payload JSONObject request body
     * @return The response from the cancelCharge service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse cancelCharge(final String chargeId, final JSONObject payload) throws AmazonPayClientException {
        return cancelCharge(chargeId, payload, null);
    }

    /**
     * The CreateRefund operation is used to create a refund for a buyer
     * and pass the Id as part of button click.
     *
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the CreateRefunds service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse createRefund(final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI createRefundsURI = Util.getServiceURI(payConfiguration, ServiceConstants.REFUNDS);
        final Map<String, String> headerMap = Util.updateHeader(header);
        return callAPI(createRefundsURI, "POST", null, payload.toString(), headerMap);
    }

    /**
     * The CreateRefund operation is used to create a refund for a buyer
     * and pass the Id as part of button click.
     *
     * @param payload JSONObject request body
     * @return The response from the CreateRefunds service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse createRefund(final JSONObject payload) throws AmazonPayClientException {
        return createRefund(payload, null);
    }

    /**
     * The getRefund operation is used to get refund details that contain
     *
     * @param refundId Refund ID provided by Checkout v2 service
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the getRefund service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse getRefund(final String refundId, final Map<String, String> header) throws AmazonPayClientException {
        final URI refundsURI = Util.getServiceURI(payConfiguration, ServiceConstants.REFUNDS);
        final URI getRefundURI = refundsURI.resolve(refundsURI.getPath() + "/" + refundId + "/");
        return callAPI(getRefundURI, "GET", null, "", header);
    }

    /**
     * The getRefund operation is used to get refund details that contain
     *
     * @param refundId Refund ID provided by Checkout v2 service
     * @return The response from the getRefund service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse getRefund(final String refundId) throws AmazonPayClientException {
        return getRefund(refundId, null);
    }


    // ----------------------------------- CV2 REPORTING APIS -----------------------------------

    /**
     * The getReports operation is used to get report details for the reports that match the filters that you specify.
     *
     * @param queryParameters Request Paramters as part of filters to be provided optionally while calling API (e.g., reportTypes, processingStatus etc.)
     * @param header Map&lt;String, String&gt; containing key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the getReports service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse getReports(final Map<String, List<String>> queryParameters, final Map<String, String> header) throws AmazonPayClientException {
        final URI getReportsURI = Util.getServiceURI(payConfiguration, ServiceConstants.REPORTS);
        final URI getReportsFinalURI = getReportsURI.resolve(getReportsURI.getPath() + "/?" + convertQueryParamters(queryParameters));
        return callAPI(getReportsFinalURI, "GET", queryParameters, "", header);
    }

    public AmazonPayResponse getReports(final Map<String, List<String>> queryParameters) throws AmazonPayClientException {
        return getReports(queryParameters, null);
    }

    public AmazonPayResponse getReports() throws AmazonPayClientException {
        return getReports(null, null);
    }

    /**
     * The getReportById operation is used to get report details for the given reportId.
     *
     * @param reportId Report ID provided while calling the API
     * @param header Map&lt;String, String&gt; containing key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the getReports service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse getReportById(final String reportId, final Map<String, String> header) throws AmazonPayClientException {
        final URI getReportByIdURI = Util.getServiceURI(payConfiguration, ServiceConstants.REPORTS);
        final URI getReportByIdFinalURI = getReportByIdURI.resolve(getReportByIdURI.getPath() + "/" + reportId);
        return callAPI(getReportByIdFinalURI, "GET", null, "", header);
    }

    public AmazonPayResponse getReportById(final String reportId) throws AmazonPayClientException {
        return getReportById(reportId, null);
    }

    /**
     * The getReportDocument operation is used to return the pre-signed S3 URL for the report. The report can be downloaded using this URL.
     *
     * @param reportDocumentId Report Document ID provided while calling the API
     * @param header Map&lt;String, String&gt; containing key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the getReports service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse getReportDocument(final String reportDocumentId, final Map<String, String> header) throws AmazonPayClientException {
        final URI getReportDocumentURI = Util.getServiceURI(payConfiguration, ServiceConstants.REPORT_DOCUMENT);
        final URI getReportDocumentFinalURI = getReportDocumentURI.resolve(getReportDocumentURI.getPath() + "/" + reportDocumentId);
        return callAPI(getReportDocumentFinalURI, "GET", null, "", header);
    }

    public AmazonPayResponse getReportDocument(final String reportDocumentId) throws AmazonPayClientException {
        return getReportDocument(reportDocumentId, null);
    }

    /**
     * The getReportSchedules operation is used to return the pre-signed S3 URL for the report. The report can be downloaded using this URL.
     *
     * @param reportTypes Report Types provided while calling the API comma-seperated list of ReportType
     * @param header Map&lt;String, String&gt; containing key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the getReports service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse getReportSchedules(final String reportTypes, final Map<String, String> header) throws AmazonPayClientException {
        final URI getReportScheduleURI = Util.getServiceURI(payConfiguration, ServiceConstants.REPORT_SCHEDULES);
        final Map<String, List<String>> queryParameters = new HashMap<>();
        if (!reportTypes.isEmpty()) {
            queryParameters.put("reportTypes", Arrays.asList(reportTypes));
        }

        final URI getReportSchedulesFinalURI = getReportScheduleURI.resolve(getReportScheduleURI.getPath() + "/?" + convertQueryParamters(queryParameters));
        return callAPI(getReportSchedulesFinalURI, "GET", queryParameters, "", header);
    }

    public AmazonPayResponse getReportSchedules(final String reportTypes) throws AmazonPayClientException {
        return getReportSchedules(reportTypes, null);
    }

    public AmazonPayResponse getReportSchedules() throws AmazonPayClientException {
        return getReportSchedules("", null);
    }

    /**
     * The getReportScheduleById operation is used to get report schedule details that match the given ID.
     *
     * @param reportScheduleId Report Schedule ID provided while calling the API
     * @param header Map&lt;String, String&gt; containing key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the getReports service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse getReportScheduleById(final String reportScheduleId, final Map<String, String> header) throws AmazonPayClientException {
        final URI getReportScheduleByIdURI = Util.getServiceURI(payConfiguration, ServiceConstants.REPORT_SCHEDULES);
        final URI getReportScheduleByIdFinalURI = getReportScheduleByIdURI.resolve(getReportScheduleByIdURI.getPath() + "/" + reportScheduleId);
        return callAPI(getReportScheduleByIdFinalURI, "GET", null, "", header);
    }

    public AmazonPayResponse getReportScheduleById(final String reportScheduleId) throws AmazonPayClientException {
        return getReportScheduleById(reportScheduleId, null);
    }

    /**
     * The createReport operation is used to submit a request to generate a report based on the reportType and date range specified.
     *
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containing key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the getReports service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse createReport(final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI createReportURI = Util.getServiceURI(payConfiguration, ServiceConstants.REPORTS);
        return callAPI(createReportURI, "POST", null, payload.toString(), header);
    }

    /**
     * The createReport operation is used to create a report schedule for the given reportType. Only one schedule per report type allowed.
     *
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containing key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the getReports service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse createReportSchedule(final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI createReportScheduleURI = Util.getServiceURI(payConfiguration, ServiceConstants.REPORT_SCHEDULES);
        return callAPI(createReportScheduleURI, "POST", null, payload.toString(), header);
    }

    /**
     * The cancelReportSchedule operation is used to cancel the report schedule with the given reportScheduleId.
     *
     * @param reportScheduleId Report Schedule ID provided while calling the API
     * @param header Map&lt;String, String&gt; containing key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the getReports service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse cancelReportSchedule(final String reportScheduleId, final Map<String, String> header) throws AmazonPayClientException {
        final URI cancelReportScheduleURI = Util.getServiceURI(payConfiguration, ServiceConstants.REPORT_SCHEDULES);
        final URI cancelReportScheduleFinalURI = cancelReportScheduleURI.resolve(cancelReportScheduleURI.getPath() + "/" + reportScheduleId);
        return callAPI(cancelReportScheduleFinalURI, "DELETE", null, "", header);
    }

    public AmazonPayResponse cancelReportSchedule(final String reportScheduleId) throws AmazonPayClientException {
        return cancelReportSchedule(reportScheduleId, null);
    }

    /**
     * The getDisbursements operation is used to receive disbursement details based on a date range of the settlement date specified in the request.
     *
     * @param queryParameters Query Parameters to be provided while calling API (e.g., startTime, endTime, pageSize, etc.)
     * @param header Map&lt;String, String&gt; containing key-value pair of headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-date)
     * @return The response from the getDisbursements API, as returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue.
     */
    public AmazonPayResponse getDisbursements(final Map<String, List<String>> queryParameters, final Map<String, String> header) throws AmazonPayClientException {
        final URI getDisbursementsURI = Util.getServiceURI(payConfiguration, ServiceConstants.DISBURSEMENTS);
        final URI getDisbursementsFinalURI = getDisbursementsURI.resolve(getDisbursementsURI.getPath() + "/?" + convertQueryParamters(queryParameters));
        return callAPI(getDisbursementsFinalURI, "GET", queryParameters, "", header);
    }

    /**
     * The finalizeCheckoutSession operation enables Pay to validate payment critical attributes and also update book-keeping attributes present in merchantMetadata
     *
     * @param checkoutSessionId Checkout Session ID provided by Checkout v2 service
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containining key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken)
     * @return The response from the CompleteCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse finalizeCheckoutSession(final String checkoutSessionId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI checkoutSessionURI = Util.getServiceURI(payConfiguration, ServiceConstants.CHECKOUT_SESSIONS);
        final URI completeCheckoutSessionURI = checkoutSessionURI.resolve(checkoutSessionURI.getPath() + "/" + checkoutSessionId + "/" + "finalize");
        return callAPI(completeCheckoutSessionURI, "POST", null, payload.toString(), header);
    }

    /**
     * The finalizeCheckoutSession operation enables Pay to validate payment critical attributes and also update book-keeping attributes present in merchantMetadata.
     *
     * @param checkoutSessionId Checkout Session ID provided by Checkout v2 service
     * @param payload JSONObject request body
     * @return The response from the CompleteCheckoutSession service API, as
     * returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse finalizeCheckoutSession(final String checkoutSessionId, final JSONObject payload) throws AmazonPayClientException {
        return finalizeCheckoutSession(checkoutSessionId, payload, null);
    }

    // Convenience function to convert List of Query parameters to String to be attached to URL
    public String convertQueryParamters(final Map<String, List<String>> parameters) throws AmazonPayClientException {
        if(parameters == null || parameters.isEmpty())
            return "";
        final StringBuilder result = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
            for (String value : entry.getValue()) {
                if (result.length() > 0) {
                    result.append("&");
                }
                result.append(entry.getKey())
                        .append("=")
                        .append(value);
            }
        }
        return result.toString();
    }

    // ----------------------------------- CV2 DISPUTE APIS -----------------------------------

    /**
     * The createDispute operation is used to notify Amazon of a newly created chargeback dispute by a buyer on a
     * transaction processed by the PSP (Payment Service Provider), ensuring the dispute is properly accounted for in
     * the Amazon Pay systems.
     *
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containing key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken, x-amz-pay-date)
     * @return The response from the createDispute service API, as returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse createDispute(final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI createDisputeURI = Util.getServiceURI(payConfiguration, ServiceConstants.DISPUTES);
        return callAPI(createDisputeURI, "POST", null, payload.toString(), header);
    }

    /**
     * The updateDispute operation is used to notify Amazon of the closure status of a chargeback dispute initiated by a
     * buyer for orders processed by a partner PSP (Payment Service Provider), ensuring proper accounting within
     * the Amazon systems.
     *
     * @param disputeId Dispute ID provided while calling the API
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containing key-value pair of required headers (e.g., keys such as x-amz-pay-authtoken, x-amz-pay-date)
     * @return The response from the updateDispute service API, as returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse updateDispute(final String disputeId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI updateDisputeURI = Util.getServiceURI(payConfiguration, ServiceConstants.DISPUTES);
        final URI updateDisputeFinalURI = updateDisputeURI.resolve(updateDisputeURI.getPath() + "/" + disputeId);
        return callAPI(updateDisputeFinalURI, "PATCH", null, payload.toString(), header);
    }

    /**
     * The updateDispute operation is used to notify Amazon of the closure status of a chargeback dispute initiated by a
     * buyer for orders processed by a partner PSP (Payment Service Provider), ensuring proper accounting within
     * the Amazon systems.
     *
     * @param disputeId Dispute ID provided while calling the API
     * @param payload JSONObject request body
     * @return The response from the updateDispute service API, as returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse updateDispute(final String disputeId, final JSONObject payload) throws AmazonPayClientException {
        return updateDispute(disputeId, payload, null);
    }

    /**
     * The contestDispute operation is used by the partner, on behalf of the merchant, to formally contest a dispute
     * managed by Amazon, requiring the submission of necessary evidence files within the specified
     * Dispute Window (11 days for Chargeback, 7 days for A-Z Claims).
     *
     * @param disputeId Dispute ID provided while calling the API
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containing key-value pair of required headers (e.g., keys such as x-amz-pay-authtoken, x-amz-pay-date)
     * @return The response from the contestDispute service API, as returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse contestDispute(final String disputeId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI contestDisputeURI = Util.getServiceURI(payConfiguration, ServiceConstants.DISPUTES);
        final URI contestDisputeFinalURI = contestDisputeURI.resolve(contestDisputeURI.getPath() + "/" + disputeId + "/contest");
        return callAPI(contestDisputeFinalURI, "POST", null, payload.toString(), header);
    }

    /**
     * The contestDispute operation is used by the partner, on behalf of the merchant, to formally contest a dispute
     * managed by Amazon, requiring the submission of necessary evidence files within the specified
     * Dispute Window (11 days for Chargeback, 7 days for A-Z Claims).
     *
     * @param disputeId Dispute ID provided while calling the API
     * @param payload JSONObject request body
     * @return The response from the contestDispute service API, as returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse contestDispute(final String disputeId, final JSONObject payload) throws AmazonPayClientException {
        return contestDispute(disputeId, payload, null);
    }

    // ----------------------------------- CV2 FILE APIS -----------------------------------

    /**
     * The uploadFile operation is utilised by PSPs (Payment Service Provider) to upload file-based evidence when a
     * merchant contests a dispute, providing the necessary reference ID to the evidence file as part of
     * the Update Dispute API process.
     *
     * @param payload JSONObject request body
     * @param header Map&lt;String, String&gt; containing key-value pair of required headers (e.g., keys such as x-amz-pay-idempotency-key, x-amz-pay-authtoken, x-amz-pay-date)
     * @return The response from the uploadFile service API, as returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse uploadFile(final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI uploadFileURI = Util.getServiceURI(payConfiguration, ServiceConstants.FILES);
        return callAPI(uploadFileURI, "POST", null, payload.toString(), header);
    }

    /**
     * The uploadFile operation is utilised by PSPs (Payment Service Provider) to upload file-based evidence when a
     * merchant contests a dispute, providing the necessary reference ID to the evidence file as part of
     * the Update Dispute API process.
     *
     * @param payload JSONObject request body
     * @return The response from the uploadFile service API, as returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to bad request or other issue
     */
    public AmazonPayResponse uploadFile(final JSONObject payload) throws AmazonPayClientException {
        return uploadFile(payload, null);
    }

}
