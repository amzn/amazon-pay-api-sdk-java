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

import java.util.Arrays;

/**
 * This represents an enum class identifying the Amazon Signature Algorithms:
 * AMZN-PAY-RSASSA-PSS and AMZN-PAY-RSASSA-PSS-V2
 */
public enum AmazonSignatureAlgorithm {
    DEFAULT("AMZN-PAY-RSASSA-PSS", 20),
    V2("AMZN-PAY-RSASSA-PSS-V2", 32);

    private final String name;
    private final int saltLength;

    private AmazonSignatureAlgorithm(String name, int saltLength){
        this.name = name;
        this.saltLength = saltLength;
    }

    /**
     * @return Returns name of the Amazon Signing Algorithm
     */
    public String getName(){
        return this.name;
    }

    /**
     * @return Returns salt length used by the Amazon Signing Algorithm
     */
    public int getSaltLength(){
        return this.saltLength;
    }

    /**
     * @param algorithm the Algorithm passed
     * @return returns Amazon Signature Algorithm object if algorithm is valid
     * @throws IllegalArgumentException if invalid algorithm is passed
     */
    public static AmazonSignatureAlgorithm returnIfValidAlgorithm(String algorithm){
        return Arrays.stream(values()).filter(signatureAlgorithm -> signatureAlgorithm.getName().equals(algorithm)).findFirst().orElseThrow(() -> new IllegalArgumentException("Not a valid algorithm"));
    }
}