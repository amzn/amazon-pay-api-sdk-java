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
 * This enum class represents reasons due to which the buyer has decided to file the dispute.
 * Dispute filing reason is one of the following:
 * ProductNotReceived, ProductUnacceptable, ProductNoLongerNeeded, CreditNotProcessed, Overcharged, DuplicateCharge,
 * SubscriptionCancelled, Unrecognized, Fraudulent, Other.
 * ProductNotReceived represents the buyer has not received the product.
 * ProductNoLongerNeeded represents the buyer no longer needs the product.
 * ProductUnacceptable represents the product is not acceptable.
 * CreditNotProcessed represents the buyer has returned the product but credit was not received.
 * Overcharged represents the buyer was overcharged for the product.
 * DuplicateCharge represents the buyer has charged twice for the same product.
 * SubscriptionCancelled represents the buyer was charged even after Subscription was cancelled.
 * Unrecognized represents the buyer does not recognize the charge.
 * Fraudulent represents the buyer was not responsible for the transaction done by their payment method.
 * Other represents any other reason for dispute filing.
 */

public enum DisputeFilingReason {
    PRODUCT_NOT_RECEIVED("ProductNotReceived"),
    PRODUCT_UNACCEPTABLE("ProductUnacceptable"),
    PRODUCT_NO_LONGER_NEEDED("ProductNoLongerNeeded"),
    CREDIT_NOT_PROCESSED("CreditNotProcessed"),
    OVERCHARGED("Overcharged"),
    DUPLICATE_CHARGE("DuplicateCharge"),
    SUBSCRIPTION_CANCELLED("SubscriptionCancelled"),
    UNRECOGNIZED("Unrecognized"),
    FRAUDULENT("Fraudulent"),
    OTHER("Other");

    private final String disputeFilingReason;

    // Constructor
    DisputeFilingReason(String disputeFilingReason) {
        this.disputeFilingReason = disputeFilingReason;
    }

    // Getter method
    public String getDisputeFilingReason() {
        return disputeFilingReason;
    }
}
