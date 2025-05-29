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
 * This enum class represents the evidence type.
 * Evidence type is one of the following:
 * PRODUCT_DESCRIPTION, RECEIPT, CANCELLATION_POLICY, CUSTOMER_SIGNATURE, TRACKING_NUMBER, OTHER.
 * ProductDescription represents the product description.
 * Receipt represents the order receipt document.
 * CancellationPolicy represents the cancellation policy document.
 * CustomerSignature represents the customer signature document.
 * TrackingNumber represents the tracking number for the product delivery.
 * Other represents other evidence type.
 */

public enum EvidenceType {
    PRODUCT_DESCRIPTION("ProductDescription"),
    RECEIPT("Receipt"),
    CANCELLATION_POLICY("CancellationPolicy"),
    CUSTOMER_SIGNATURE("CustomerSignature"),
    TRACKING_NUMBER("TrackingNumber"),
    CARRIER_NAME("CarrierName"),
    DEVICE_ID("DeviceId"),
    DEVICE_NAME("DeviceName"),
    DOWNLOAD_DATE_TIME("DownloadDateTime"),
    OTHER("Other");

    private final String evidenceType;

    // Constructor
    EvidenceType(String evidenceType) {
        this.evidenceType = evidenceType;
    }

    // Getter method
    public String getEvidenceType() {
        return evidenceType;
    }
}
