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


import com.amazon.pay.api.types.Region;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServiceConstants {
    public static final Map<Region, String> endpointMappings;
    public static final Map<String, Integer> serviceErrors;

    public static final String APPLICATION_LIBRARY_VERSION = "4.3.0";
    public static final String GITHUB_SDK_NAME = "amazon-pay-sdk-v2-java";
    public static final String AMAZON_PAY_API_VERSION = "v1";

    public static final String AMAZON_SIGNATURE_ALGORITHM = "AMZN-PAY-RSASSA-PSS";
    public static final String HASH_ALGORITHM = "SHA-256";
    public static final String SIGNATURE_ALGORITHM = "SHA256WithRSA/PSS";
    public static final String MASK_GENERATION_FUNCTION = "MGF1";

    public static final String PRIVATE_KEY = "Private Key";
    public static final String REGION = "Region";
    public static final String PUBLIC_KEY_ID = "Public key id";
    public static final String REDACTED = "Redacted";

    public static final String DELIVERY_TRACKERS = "v1/deliveryTrackers";

    public static final String MERCHANT_SCAN = "in-store/v1/merchantScan";
    public static final String REFUND = "in-store/v1/refund";
    public static final String CHARGE = "in-store/v1/charge";

    public static final String CHECKOUT = "v1/checkoutSessions";
    public static final String CHARGE_PERMISSIONS = "v1/chargePermissions";
    public static final String CHARGES = "v1/charges";
    public static final String REFUNDS = "v1/refunds";

    public static final int RESPONSE_STATUS_CODE = 0;
    public static final int RESPONSE_STRING = 1;
    public static final int REQUEST_ID = 2;

    static {
        Map<Region, String> endpointMappingsMap = new HashMap<>();
        endpointMappingsMap.put(Region.EU, "https://pay-api.amazon.eu");
        endpointMappingsMap.put(Region.JP, "https://pay-api.amazon.jp");
        endpointMappingsMap.put(Region.NA, "https://pay-api.amazon.com");
        endpointMappings = Collections.unmodifiableMap(endpointMappingsMap);
    }

    static {
        Map<String, Integer> serviceErrorsMap = new HashMap<>();
        serviceErrorsMap.put("Internal Server Error", HttpURLConnection.HTTP_INTERNAL_ERROR);
        serviceErrorsMap.put("Service Unavailable", HttpURLConnection.HTTP_UNAVAILABLE);
        serviceErrors = Collections.unmodifiableMap(serviceErrorsMap);
    }

}
