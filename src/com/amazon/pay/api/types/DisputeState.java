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
 * This enum class represents the dispute state.
 * Dispute state is one of the following:
 * UNDER_REVIEW, ACTION_REQUIRED, RESOLVED, CLOSED.
 * UnderReview represents the dispute is under investigation.
 * ActionRequired represents pending merchant or buyer action.
 * Resolved represents the dispute is resolved and reached a logical resolution.
 * Closed represents the dispute is closed and immutable.
 */
public enum DisputeState {
    UNDER_REVIEW("UnderReview"),
    ACTION_REQUIRED("ActionRequired"),
    RESOLVED("Resolved"),
    CLOSED("Closed");

    private final String disputeState;

    // Constructor
    DisputeState(String disputeState) {
        this.disputeState = disputeState;
    }

    // Getter method
    public String getDisputeState() {
        return disputeState;
    }
}
