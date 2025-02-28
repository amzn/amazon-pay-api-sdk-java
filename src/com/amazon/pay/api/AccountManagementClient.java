package com.amazon.pay.api;

import com.amazon.pay.api.exceptions.AmazonPayClientException;
import org.json.JSONObject;

import java.net.URI;
import java.util.Map;

public class AccountManagementClient extends AmazonPayClient {

    public AccountManagementClient(PayConfiguration payConfiguration) throws AmazonPayClientException {
        super(payConfiguration);
    }

    /**
     * Creates a new merchant account in Amazon Pay. The request body should contain necessary details to register
     * the merchant account. This API is used by partners to onboard new merchants.
     *
     * @param payload JSONObject containing the request body with merchant details.
     * @param header  Map<String, String> containing key-value pairs. Optional headers (e.g., x-amz-pay-authtoken)
     * @return The response from the createMerchantAccount API, as returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to a bad request or other issue.
     */
    public AmazonPayResponse createMerchantAccount(final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI createMerchantAccountURI = Util.getServiceURI(payConfiguration, ServiceConstants.ACCOUNT_MANAGEMENT);
        final Map<String, String> headerMap = Util.updateHeader(header);
        return callAPI(createMerchantAccountURI, "POST", null, payload.toString(), headerMap);
    }

    /**
     * Updates a merchant account for the given Merchant Account ID.
     *
     * @param merchantAccountId Internal Merchant Account ID provided while calling the API.
     * @param payload JSONObject containing the request body with updated merchant account details.
     * @param header  Map<String, String> containing key-value pairs of required headers (e.g., x-amz-pay-authtoken)
     * @return The response from the updateMerchantAccount API, as returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to a bad request or other issue.
     */
    public AmazonPayResponse updateMerchantAccount(final String merchantAccountId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI merchantAccountURI = Util.getServiceURI(payConfiguration, ServiceConstants.ACCOUNT_MANAGEMENT);
        final URI updateMerchantAccountURI = merchantAccountURI.resolve(merchantAccountURI.getPath() + "/" + merchantAccountId);
        final Map<String, String> headerMap = Util.updateHeader(header);
        return callAPI(updateMerchantAccountURI, "PATCH", null, payload.toString(), headerMap);
    }

    /**
     * Claims an existing merchant account using the provided Merchant Account ID.
     *
     * @param merchantAccountId Internal Merchant Account ID for the claim request.
     * @param payload JSONObject containing the request body required for the claim process.
     * @param header  Map<String, String> containing key-value pairs of optional headers
     * @return The response from the merchantAccountClaim API, as returned by Amazon Pay.
     * @throws AmazonPayClientException When an error response is returned by Amazon Pay due to a bad request or other issue.
     */
    public AmazonPayResponse merchantAccountClaim(final String merchantAccountId, final JSONObject payload, final Map<String, String> header) throws AmazonPayClientException {
        final URI merchantAccountURI = Util.getServiceURI(payConfiguration, ServiceConstants.ACCOUNT_MANAGEMENT);
        final URI merchantAccountClaimURI = merchantAccountURI.resolve(merchantAccountURI.getPath() + "/" + merchantAccountId + "/claim");
        final Map<String, String> headerMap = Util.updateHeader(header);
        return callAPI(merchantAccountClaimURI, "POST", null, payload.toString(), headerMap);
    }
}
