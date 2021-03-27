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

import com.amazon.pay.api.types.Environment;
import com.amazon.pay.api.types.Region;

import net.sf.json.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.security.spec.X509EncodedKeySpec;

public class GenerateButtonSignatureTest {

    private WebstoreClient client;
    private Signature signature;

    private static final String PLAIN_TEXT = "AMZN-PAY-RSASSA-PSS\n8dec52d799607be40f82d5c8e7ecb6c171e6591c41b1111a576b16076c89381c";

    @Before
    public void setUp() throws Exception {
        // Load private key and configure client
        final PayConfiguration config = new PayConfiguration()
                .setPrivateKey(new String(Files.readAllBytes(
                Paths.get("tst/com/amazon/pay/api/unit_test_private_key.txt"))).toCharArray())
                .setRegion(Region.NA)
                .setPublicKeyId("ABCDEF0000000000000")
                .setEnvironment(Environment.SANDBOX);
        client = new WebstoreClient(config);

        // Load public key and prepare verifier
        Security.addProvider(new BouncyCastleProvider());
        final PemReader pemReader = new PemReader(new StringReader(new String(Files.readAllBytes(
            Paths.get("tst/com/amazon/pay/api/unit_test_public_key.txt")))));
        final PemObject pemObject = pemReader.readPemObject();
        pemReader.close();
        final X509EncodedKeySpec spec = new X509EncodedKeySpec(pemObject.getContent());
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final PublicKey publicKey = keyFactory.generatePublic(spec);
        final MGF1ParameterSpec mgf1ParameterSpec = new MGF1ParameterSpec(ServiceConstants.HASH_ALGORITHM);
        final PSSParameterSpec pssParameterSpec = new PSSParameterSpec(ServiceConstants.HASH_ALGORITHM,
                ServiceConstants.MASK_GENERATION_FUNCTION, mgf1ParameterSpec, SignatureHelper.SALT_LENGTH, SignatureHelper.TRAILER_FIELD);
        signature = Signature.getInstance(ServiceConstants.SIGNATURE_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
        signature.setParameter(pssParameterSpec);
        signature.initVerify(publicKey);
    }

    @Test
    public void testButtonSignatureWithString() throws Exception {
        final String payload = "{\"storeId\":\"amzn1.application-oa2-client.xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\",\"webCheckoutDetails\":{\"checkoutReviewReturnUrl\":\"https://localhost/test/CheckoutReview.php\",\"checkoutResultReturnUrl\":\"https://localhost/test/CheckoutResult.php\"}}";
        final String signatureString = client.generateButtonSignature(payload);
        signature.update(PLAIN_TEXT.getBytes());
        Assert.assertTrue(signature.verify(Base64.decode(signatureString)));
    }

    @Test
    public void testButtonSignatureWithJSONObject() throws Exception {
        final JSONObject payload = new JSONObject();
        payload.put("storeId", "amzn1.application-oa2-client.xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        final JSONObject webCheckoutDetails = new JSONObject();
        webCheckoutDetails.put("checkoutReviewReturnUrl", "https://localhost/test/CheckoutReview.php");
        webCheckoutDetails.put("checkoutResultReturnUrl", "https://localhost/test/CheckoutResult.php");
        payload.put("webCheckoutDetails", webCheckoutDetails);

        final String signatureString = client.generateButtonSignature(payload);
        signature.update(PLAIN_TEXT.getBytes());
        Assert.assertTrue(signature.verify(Base64.decode(signatureString)));
    }

}
