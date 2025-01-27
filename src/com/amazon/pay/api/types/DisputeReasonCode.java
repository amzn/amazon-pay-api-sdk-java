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

package com.amazon.pay.api.types;

/**
 * This enum class represents the dispute reason code.
 * Dispute reason code is one of the following:
 * MERCHANT_RESPONSE_REQUIRED, MERCHANT_ADDITIONAL_EVIDENCES_REQUIRED,
 * BUYER_ADDITIONAL_EVIDENCES_REQUIRED, MERCHANT_ACCEPTED_DISPUTE,
 * MERCHANT_RESPONSE_DEADLINE_EXPIRED, BUYER_CANCELLED,
 * INVESTIGATOR_RESOLVED, AUTO_RESOLVED, CHARGEBACK_FILED.
 * MERCHANT_RESPONSE_REQUIRED represents the merchant needs to take action by either accepting or contesting the dispute.
 * MERCHANT_ADDITIONAL_EVIDENCES_REQUIRED represents the merchant needs to provide additional evidence.
 * BUYER_ADDITIONAL_EVIDENCES_REQUIRED represents the buyer needs to provide additional evidence.
 * MERCHANT_ACCEPTED_DISPUTE represents the merchant has accepted the dispute.
 * MERCHANT_RESPONSE_DEADLINE_EXPIRED represents the merchant did not respond within the expected window and dispute has been closed in buyer's favour.
 * BUYER_CANCELLED represents the buyer has cancelled the dispute request.
 * INVESTIGATOR_RESOLVED represents the dispute resolved by the investigator.
 * AUTO_RESOLVED represents the dispute was automatically resolved.
 * CHARGEBACK_FILED represents the buyer has filed chargeback besides claim dispute.
 */

public enum DisputeReasonCode {
    MERCHANT_RESPONSE_REQUIRED("MerchantResponseRequired"),
    MERCHANT_ADDITIONAL_EVIDENCES_REQUIRED("MerchantAdditionalEvidencesRequired"),
    BUYER_ADDITIONAL_EVIDENCES_REQUIRED("BuyerAdditionalEvidencesRequired"),
    MERCHANT_ACCEPTED_DISPUTE("MerchantAcceptedDispute"),
    MERCHANT_RESPONSE_DEADLINE_EXPIRED("MerchantResponseDeadlineExpired"),
    BUYER_CANCELLED("BuyerCancelled"),
    INVESTIGATOR_RESOLVED("InvestigatorResolved"),
    AUTO_RESOLVED("AutoResolved"),
    CHARGEBACK_FILED("ChargebackFiled");

    private final String disputeReasonCode;

    // Constructor
    DisputeReasonCode(String disputeReasonCode) {
        this.disputeReasonCode = disputeReasonCode ;
    }

    // Getter method
    public String getDisputeReasonCode() {
        return disputeReasonCode;
    }
}