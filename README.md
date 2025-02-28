[![Maven Central](https://maven-badges.herokuapp.com/maven-central/software.amazon.pay/amazon-pay-api-sdk-java/badge.svg)](https://maven-badges.herokuapp.com/maven-central/software.amazon.pay/amazon-pay-api-sdk-java)

# Amazon Pay API SDK (Java)
Amazon Pay Integration

## Requirements

* Amazon Pay - [Register here](https://pay.amazon.com/signup)
* Java 8 or higher

## SDK Installation

To use the SDK in a Maven project, add a <dependency> reference in your pom.xml file's <dependencies> section:

```xml
<dependencies>
    <dependency>
        <groupId>software.amazon.pay</groupId>
        <artifactId>amazon-pay-api-sdk-java</artifactId>
        <version>2.6.6</version>
    </dependency>
</dependencies>
```

To use the SDK in a Gradle project, add the following line to your build.gradle file::

```
implementation 'software.amazon.pay:amazon-pay-api-sdk-java:2.6.6'
```

For legacy projects, you can just grab the binary [jar file](https://github.com/amzn/amazon-pay-api-sdk-java/releases) from the GitHub Releases page.

## Public and Private Keys

MWS access keys, MWS secret keys, and MWS authorization tokens from previous MWS-based integrations cannot be used with this SDK.

You will need to generate your own public/private key pair to make API calls with this SDK.

In Windows 10 this can be done with ssh-keygen commands:

```
ssh-keygen -t rsa -b 2048 -f private.pem
ssh-keygen -f private.pem -e -m PKCS8 > public.pub
```

In Linux or macOS this can be done using openssl commands:

```
openssl genrsa -out private.txt 2048
openssl rsa -in private.txt -pubout > public.pub
```

The first command above generates a private key and the second line uses the private key to generate a public key.

To associate the key with your account, follow the instructions here to
[Get your Public Key ID](https://developer.amazon.com/docs/amazon-pay-checkout/get-set-up-for-integration.html#5-get-your-public-key-id).

## Namespace

Namespace for this package is com.amazon.pay.api to differentiate this SDK from the original Amazon Pay MWS SDK that uses just the com.amazon.pay namespace.

Here are some common imports you may need depending on your situation:

```java
import com.amazon.pay.api.AmazonPayClient;
import com.amazon.pay.api.AmazonPayResponse;
import com.amazon.pay.api.InstoreClient; // not needed if using WebstoreClient
import com.amazon.pay.api.PayConfiguration;
import com.amazon.pay.api.RequestSigner; // not needed if using WebstoreClient
import com.amazon.pay.api.WebstoreClient;
import com.amazon.pay.api.exceptions.AmazonPayClientException;
import com.amazon.pay.api.types.Environment;
import com.amazon.pay.api.types.Region;

// to construct/parse arbitrary JSON objects
import org.json.JSONArray;
import org.json.JSONObject;

// for generating an idempotency key
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// for loading your private key
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
```

## Versioning

The pay-api.amazon.com|eu|jp endpoint uses versioning to allow future updates.  The major version of this SDK will stay aligned with the API version of the endpoint.

If you are using version 1.x.y of this SDK, $version in below examples would be "v1". 2.x.y would be "v2", etc.

## Configuration

You will need your public key id and the file path to your private key.

You will need to specify either Environment.SANDBOX for Sandbox mode, or Environment.LIVE for Production mode.

```java
PayConfiguration payConfiguration = null;
try {
    payConfiguration = new PayConfiguration()
                .setPublicKeyId("YOUR_PUBLIC_KEY_ID")
                .setRegion(Region.YOUR_REGION_CODE)
                .setPrivateKey("YOUR_PRIVATE_KEY_STRING".toCharArray())
                .setEnvironment(Environment.SANDBOX)
                .setAlgorithm("AMZN-PAY-RSASSA-PSS-V2"); // Amazon Signing Algorithm, Optional: uses AMZN-PAY-RSASSA-PSS if not specified
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

// If you have created envrionment specific keys (i.e Public Key Starts with LIVE or SANDBOX) in seller central, then use those PublicKeyId & PrivateKey. In this case, no need to set Environment
PayConfiguration payConfiguration = null;
try {
    payConfiguration = new PayConfiguration()
                .setPublicKeyId("YOUR_PUBLIC_KEY_ID") // LIVE-XXXXX or SANDBOX-XXXXX
                .setRegion(Region.YOUR_REGION_CODE)
                .setPrivateKey("YOUR_PRIVATE_KEY_STRING".toCharArray())
                .setAlgorithm('AMZN-PAY-RSASSA-PSS-V2'); // Amazon Signing Algorithm, Optional: uses AMZN-PAY-RSASSA-PSS if not specified
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

// If you have your private key in a file, you can set it in the payConfiguration in the following way:

try {
    payConfiguration = new PayConfiguration()
                   .setPublicKeyId("YOUR_PUBLIC_KEY_ID")
                   .setRegion(Region.YOUR_REGION_CODE)
                   .setPrivateKey(new String(Files.readAllBytes(Paths.get("private.pem"))).toCharArray())
                   .setEnvironment(Environment.SANDBOX)
                   .setAlgorithm('AMZN-PAY-RSASSA-PSS-V2'); // Amazon Signing Algorithm, Optional: uses AMZN-PAY-RSASSA-PSS if not specified
} catch (AmazonPayClientException e) {
     e.printStackTrace();
}

// You can also set your private key as a java.security.PrivateKey object

PrivateKey privateKey = ...

try {
    payConfiguration = new PayConfiguration()
                .setPublicKeyId("YOUR_PUBLIC_KEY_ID")
                .setRegion(Region.YOUR_REGION_CODE)
                .setPrivateKey(privateKey)
                .setEnvironment(Environment.SANDBOX)
                .setAlgorithm('AMZN-PAY-RSASSA-PSS-V2'); // Amazon Signing Algorithm, Optional: uses AMZN-PAY-RSASSA-PSS if not specified
} catch (AmazonPayClientException e) {
     e.printStackTrace();
}

// If you have want to enable proxy support, you can set it in the payConfiguration in the following way:

try {
    ProxySettings proxySettings = new ProxySettings()
                .setProxyHost("localhost")
                .setProxyPort(8080)
                .setProxyUser("user")
                .setProxyPassword("password".toCharArray());
    
    payConfiguration = new PayConfiguration()
                   .setPublicKeyId("YOUR_PUBLIC_KEY_ID")
                   .setRegion(Region.YOUR_REGION_CODE)
                   .setPrivateKey("YOUR_PRIVATE_KEY_STRING".toCharArray())
                   .setEnvironment(Environment.SANDBOX)
                   .setAlgorithm('AMZN-PAY-RSASSA-PSS-V2'); // Amazon Signing Algorithm, Optional: uses AMZN-PAY-RSASSA-PSS if not specified
                   .setProxySettings(proxySettings);
} catch (AmazonPayClientException e) {
     e.printStackTrace();
}

// If you want to enable the Custom Connection Pool, you can set it in the payConfiguration in the following way:

try {
    int MAX_CLIENT_CONNECTIONS = 30; // This value should be decided according to your requirement
    payConfiguration = new PayConfiguration()
                    .setPublicKeyId("YOUR_PUBLIC_KEY_ID")
                    .setRegion(Region.YOUR_REGION_CODE) 
                    .setPrivateKey("YOUR_PRIVATE_KEY_STRING".toCharArray())
                    .setEnvironment(Environment.SANDBOX)
                    .setClientConnections(MAX_CLIENT_CONNECTIONS); // Default is set to 20
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

// If you want to set client request configuration, connection, connect and socket timeout, the payConfiguration looks like below:

try {
    payConfiguration = new PayConfiguration()
                    .setPublicKeyId("YOUR_PUBLIC_KEY_ID")
                    .setRegion(Region.YOUR_REGION_CODE)
                    .setPrivateKey("YOUR_PRIVATE_KEY_STRING".toCharArray())
                    .setEnvironment(Environment.SANDBOX)
                    .setRequestConfig(new RequestConfig(1000, 2000, 4000);//connection timeout = 1s, connect timeout = 2s, socket timeout = 4s
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}
```

# Convenience Functions (Overview)

Make use of the built-in convenience functions to easily make API calls.  Scroll down further to see example code snippets.

When using the convenience functions, the request payload will be signed using the provided private key, and a HTTPS request is made to the correct regional endpoint.
In the event of request throttling, the HTTPS call will be attempted up to three times using an exponential backoff approach.

## Alexa Delivery Trackers API
[Amazon Pay Delivery Notifications Integration Guide](https://developer.amazon.com/docs/amazon-pay-onetime/delivery-order-notifications.html).

* AmazonPayClient: **deliveryTracker**(JSONObject payload, Map<String, String> header) &#8594; POST to "$version/deliveryTrackers"

## Amazon Checkout v2 API
[Amazon Pay Checkout v2 Integration Guide](https://developer.amazon.com/docs/amazon-pay-api-v2/introduction.html)

### Amazon Checkout v2 Buyer object
* WebstoreClient: **getBuyer**(String buyerToken, Map<String, String> header) &#8594; GET to "$version/buyers/$buyerToken"

### Amazon Checkout v2 CheckoutSession object
* WebstoreClient: **createCheckoutSession**(JSONObject payload, Map<String, String> header) &#8594; POST to "$version/checkoutSessions"
* WebstoreClient: **getCheckoutSession**(String checkoutSessionId, Map<String, String> header) &#8594; GET to "$version/checkoutSessions/$checkoutSessionId"
* WebstoreClient: **updateCheckoutSession**(String checkoutSessionId, JSONObject payload, Map<String, String> header) &#8594; PATCH to "$version/checkoutSessions/$checkoutSessionId"
* WebstoreClient: **completeCheckoutSession**(String checkoutSessionId, JSONObject payload, Map<String, String> header) &#8594; POST to "$version/checkoutSessions/$checkoutSessionId/complete"

### Amazon Checkout v2 ChargePermission object
* WebstoreClient: **getChargePermission**(String chargePermissionId, Map<String, String> header) &#8594; GET to "$version/chargePermissions/$chargePermissionId"
* WebstoreClient: **updateChargePermission**(String chargePermissionId, JSONObject payload, Map<String, String> header) &#8594; PATCH to "$version/chargePermissions/$chargePermissionId"
* WebstoreClient: **closeChargePermission**(String chargePermissionId, JSONObject payload, Map<String, String> header) &#8594; DELETE to "$version/chargePermissions/$chargePermissionId/close"

### Amazon Checkout v2 Charge object
* WebstoreClient: **createCharge**(payload, Map<String, String> header) &#8594; POST to "$version/charges"
* WebstoreClient: **getCharge**(String chargeId, Map<String, String> header) &#8594; GET to "$version/charges/$chargeId"
* WebstoreClient: **updateCharge**(String chargeId, JSONObject payload, Map<String, String> header) &#8594; PATCH to "$version/charges/$chargeId"
* WebstoreClient: **captureCharge**(String chargeId, JSONObject payload, Map<String, String> header) &#8594; POST to "$version/charges/$chargeId/capture"
* WebstoreClient: **cancelCharge**(String chargeId, JSONObject payload, Map<String, String> header) &#8594; DELETE to "$version/charges/$chargeId/cancel"

### Amazon Checkout v2 Refund object
* WebstoreClient: **createRefund**(payload, Map<String, String> header) &#8594; POST to "$version/refunds"
* WebstoreClient: **getRefund**(String refundId, Map<String, String> header) &#8594; GET to "$version/refunds/$refundId"

### Amazon Checkout v2 Dispute object
* WebstoreClient: **createDispute**(JSONObject payload, Map<String, String> header) &#8594; POST to "$version/disputes"
* WebstoreClient: **updateDispute**(String disputeId, JSONObject payload, Map<String, String> header) &#8594; PATCH to "$version/disputes/$disputeId"
* WebstoreClient: **contestDispute**(String disputeId, JSONObject payload, Map<String, String> header) &#8594; POST to "$version/disputes/$disputeId/contest"
* WebstoreClient: **uploadFile**(JSONObject payload, Map<String, String> header) &#8594; POST to "$version/files"

## In-Store API
Please contact your Amazon Pay Account Manager before using the In-Store API calls in a Production environment to obtain a copy of the In-Store Integration Guide.

* InstoreClient: **merchantScan**(JSONObject scanRequest, Map<String, String> header) &#8594; POST to "$version/in-store/merchantScan"
* InstoreClient: **charge**(JSONObject chargeRequest, Map<String, String> header) &#8594; POST to "$version/in-store/charge"
* InstoreClient: **refund**(JSONObject refundRequest, Map<String, String> header) &#8594; POST to "$version/in-store/refund"

## Authorization Tokens API
Please note that your solution provider account must have a pre-existing relationship (valid and active MWS authorization token) with the merchant account in order to use this function.

* AmazonPayClient: **getAuthorizationToken**(String mwsAuthToken, String merchantId, Map<String, String> header) &#8594; GET to "$version/authorizationTokens/$mwsAuthToken?merchantId=$merchantId"

 ### Amazon Checkout v2 Merchant Onboarding & Account Management object
* WebstoreClient: **registerAmazonPayAccount**(JSONObject payload, Map<String, String> header) &#8594; POST to "$version/merchantAccounts"
* WebstoreClient: **updateAmazonPayAccount**(String merchantAccountId, JSONObject payload, Map<String, String> header) &#8594; PATCH to "$version/merchantAccounts/$merchantAccountId"
* WebstoreClient: **deleteAmazonPayAccount**(String merchantAccountId, Map<String, String> header) &#8594; DELETE to "$version/merchantAccounts/$merchantAccountId"

## Single Page Checkout API
* WebstoreClient: **finalizeCheckoutSession**(String checkoutSessionId, JSONObject payload, Map<String, String> header) &#8594; POST to "$version/checkoutSessions/$checkoutSessionId/finalize"

### Amazon Checkout v2 Account Management APIs
* **createMerchantAccount**(payload, headers) &#8594; POST to "version/merchantAccounts"
* **updateMerchantAccount**(merchantAccountId, payload, headers) &#8594; PATCH to "version/merchantAccounts/merchantAccountId"
* **merchantAccountClaim**(merchantAccountId, payload, headers) &#8594; POST to "version/merchantAccounts/merchantAccountId/claim"

# Using Convenience Functions

Four quick steps are needed to make an API call:

Step 1. Construct a AmazonPayClient (using the previously defined PayConfiguration object).

```java
AmazonPayClient client = new AmazonPayClient(payConfiguration);
    // -or-
WebstoreClient webstoreClient = new WebstoreClient(payConfiguration);
    // -or-
InstoreClient instoreClient = new InstoreClient(payConfiguration);
```

 Step 2. Generate the payload.

```java
JSONObject payload = new JSONObject();
payload.put("scanData", "UKhrmatMeKdlfY6b");
payload.put("scanReferenceId", "0b8fb271-2ae2-49a5-b35d890");
payload.put("merchantCOE", "DE");
payload.put("ledgerCurrency", "EUR");
```

 Step 3. Execute the call.

```java
AmazonPayResponse response = instoreClient.merchantScan(payload);
```

  Step 4. Check the result.

   response will be an object with the following getters:

   * '**getStatus()**' - int HTTP status code (200, 201, etc.)
   * '**getResponse()**' - the response serialized into a JSONObject
   * '**getRawResponse()**' - the raw JSON String response body received from Amazon Pay
   * '**getRequestId()**' - the Request ID from Amazon API gateway
   * '**getUrl()**' - the URL for the REST call the SDK calls, for troubleshooting purposes
   * '**getMethod()** - POST, GET, PATCH, or DELETE
   * '**getHeaders()**' - an Map<String, String> containing the various headers generated by the SDK, for troubleshooting purposes
   * '**getRawRequest()**' - the JSON request body String sent to Amazon Pay
   * '**getRetries()**' - usually 0, but reflects the number of times a request was retried due to throttling or other server-side issue
   * '**getDuration()**' - duration in milliseconds of SDK function call
   * '**isSuccess()**' - returns true for a 200, 201, or 202 HTTP status; false otherwise

   The first two items (status, response) are critical.  The remaining items are useful in troubleshooting situations.

   If you are a Solution Provider and need to make an API call on behalf of a different merchant account, you will need to pass along an extra authentication token parameter into the API call.

# Convenience Functions Code Samples

## Amazon Pay Alexa Delivery Notifications

[Delivery Notifications Integration Guide](https://developer.amazon.com/docs/amazon-pay-onetime/delivery-order-notifications.html).

The deliveryTracker API is only avialable in live environment (not sandbox).

This method is available in the base client ("AmazonPayClient").

```java

AmazonPayResponse response = null;

JSONObject payload = new JSONObject();
JSONObject deliveryDetails = new JSONObject();
JSONArray deliveryDetailsArray = new JSONArray();
deliveryDetails.put("trackingNumber", "0430955041235");
deliveryDetails.put("carrierCode", "FEDEX");
deliveryDetailsArray.put(deliveryDetails);
payload.put("amazonOrderReferenceId", "P01-8845762-6072995");
payload.put("deliveryDetails", deliveryDetailsArray);

try {
     response = client.deliveryTracker(payload);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```
## Amazon Pay Checkout v2 API
[Checkout v2 Integration Guide](https://developer.amazon.com/docs/amazon-pay-api-v2/introduction.html)

These methods are available in Webstore Client.

The headers field is not optional for create/POST calls because it requires, at a minimum, the x-amz-pay-idempotency-key header:
```java
Map<String,String> header = new HashMap<String,String>();
header.put("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));
```

### Making a createCheckoutSession request

```java
JSONObject payload = new JSONObject();
JSONObject webCheckoutDetails = new JSONObject();
webCheckoutDetails.put("checkoutReviewReturnUrl", "https://localhost/store/checkout_review");
payload.put("webCheckoutDetails", webCheckoutDetails);
payload.put("storeId", "amzn1.application-oa2-client.000000000000000000000000000000000");
AmazonPayResponse response = null;
String checkoutSessionId = null;

Map<String,String> header = new HashMap<String,String>();
header.put("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));
try {
     response = webstoreClient.createCheckoutSession(payload, header);
     checkoutSessionId = response.getResponse().getString("checkoutSessionId");
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}
```

### Making a getCheckoutSession request

```java

AmazonPayResponse response = null;
String checkoutSessionId = "00000000-0000-0000-0000-000000000000";

try {
     response = webstoreClient.getCheckoutSession(checkoutSessionId);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```

### Making an updateCheckoutSession request

```java

AmazonPayResponse response = null;
String checkoutSessionId = "00000000-0000-0000-0000-000000000000";

JSONObject payload = new JSONObject();
JSONObject updateWebCheckoutDetails = new JSONObject();
updateWebCheckoutDetails.put("checkoutResultReturnUrl", "https://localhost/store/checkout_return");
payload.put("webCheckoutDetails", updateWebCheckoutDetails);

JSONObject paymentDetails = new JSONObject();
paymentDetails.put("paymentIntent" , "Authorize");
paymentDetails.put("canHandlePendingAuthorization", false);
JSONObject chargeAmount = new JSONObject();
chargeAmount.put("amount", "12.34");
chargeAmount.put("currencyCode", "USD");
paymentDetails.put("chargeAmount", chargeAmount);
payload.put("paymentDetails", paymentDetails);

JSONObject merchantMetadata = new JSONObject();
merchantMetadata.put("merchantReferenceId", "2019-0001");
merchantMetadata.put("merchantStoreName", "AmazonTestStoreFront");
merchantMetadata.put("noteToBuyer", "noteToBuyer");
merchantMetadata.put("customInformation", "custom information goes here");
payload.put("merchantMetadata", merchantMetadata);

try {
     response = webstoreClient.updateCheckoutSession(checkoutSessionId, payload);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```

### Making a completeCheckoutSession request

```java

AmazonPayResponse response = null;
String checkoutSessionId = "00000000-0000-0000-0000-000000000000";

JSONObject payload = new JSONObject();
JSONObject chargeAmount = new JSONObject();
chargeAmount.put("amount", "14.00");
chargeAmount.put("currencyCode", "USD");
payload.put("chargeAmount", chargeAmount);

try {
     response = webstoreClient.completeCheckoutSession(checkoutSessionId, payload);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```

### Making an getChargePermissions request

```java

AmazonPayResponse response = null;
String chargePermissionId = "S01-0000000-0000000";

try {
     response = webstoreClient.getChargePermissions(chargePermissionId);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```

### Making a updateChargePermission request

```java

AmazonPayResponse response = null;
String chargePermissionId = "S01-0000000-0000000";

JSONObject payload = new JSONObject();
JSONObject merchantMetadata = new JSONObject();
merchantMetadata.put("merchantReferenceId", "32-41-323141");
merchantMetadata.put("merchantStoreName", "AmazonTestStoreName");
merchantMetadata.put("noteToBuyer", "Some note to buyer");
merchantMetadata.put("customInformation", "This is custom info");
payload.put("merchantMetadata", merchantMetadata);

try {
     response = webstoreClient.updateChargePermission(chargePermissionId, payload);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```

### Making a closeChargePermission request

```java

AmazonPayResponse response = null;
String chargePermissionId = "S01-0000000-0000000";

JSONObject payload = new JSONObject();
payload.put("closureReason", "Specify the reason here");
payload.put("cancelPendingCharges", "false");

try {
     response = webstoreClient.closeChargePermission(chargePermissionId, payload);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```

### Making a getCharge request

```java

AmazonPayResponse response = null;
String chargeId = "S01-0000000-0000000-C000000";

try {
     response = webstoreClient.getCharge(chargeId);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```

### Making a createCharge request

```java

AmazonPayResponse response = null;

JSONObject payload = new JSONObject();
JSONObject chargeAmount = new JSONObject();
chargeAmount.put("amount", "1.23");
chargeAmount.put("currencyCode", "USD");

payload.put("chargePermissionId", "S01-0000000-0000000");
payload.put("chargeAmount", chargeAmount);
payload.put("captureNow", false);
// if payload.put("captureNow", true);
// then provide
// payload.put("softDescriptor", "My Soft Descriptor");
payload.put("canHandlePendingAuthorization", true);

String chargeId = null;

Map<String, String> header = new HashMap<String, String>();
header.put("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));

try {
     response = webstoreClient.createCharge(payload, header);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}
chargeId = response.getResponse().getString("chargeId");

```

### Making a updateCharge request

```java

final String chargeId = "S03-0000000-0000000-C000000";

final Map<String, String> header = Collections.singletonMap("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));

JSONObject payload = new JSONObject();

JSONObject statusDetails = new JSONObject();
statusDetails.put("state", "Canceled");
statusDetails.put("reasonCode", "ExpiredUnused");

payload.put("statusDetails", statusDetails);

try {
    final AmazonPayResponse response =  webstoreClient.updateCharge(chargeId, payload, header);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```

### Making a captureCharge request

```java

AmazonPayResponse response = null;
String chargeId = "S01-0000000-0000000-C000000";

JSONObject payload = new JSONObject();
JSONObject captureAmount = new JSONObject();
captureAmount.put("amount", "1.23");
captureAmount.put("currencyCode", "USD");
payload.put("captureAmount", captureAmount);
payload.put("softDescriptor", "My Soft Descriptor");

Map<String, String> header = new HashMap<String, String>();
header.put("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));

try {
     response = webstoreClient.captureCharge(chargeId, payload, header);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```

### Making a cancelCharge request

```java

AmazonPayResponse response = null;
String chargeId = "S01-0000000-0000000-C000000";

JSONObject payload = new JSONObject();
payload.put("cancellationReason", "Buyer changed their mind");

try {
     response = webstoreClient.cancelCharge(chargeId, payload);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```

### Making a createRefund request

```java

AmazonPayResponse response = null;
String chargeId = "S01-0000000-0000000-C000000";

JSONObject payload = new JSONObject();
JSONObject refundAmount = new JSONObject();
refundAmount.put("amount", "0.01");
refundAmount.put("currencyCode", "USD");
payload.put("chargeId", chargeId);
payload.put("refundAmount", refundAmount);
payload.put("softDescriptor", "AMZ*soft");

Map<String,String> header = new HashMap<String,String>();
header.put("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));
String refundId = null;

try {
     response = webstoreClient.createRefund(payload,header);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}
refundId = response.getResponse().getString("refundId");

```

### Making a getRefund request

```java

AmazonPayResponse response = null;
String refundId = "S01-0000000-0000000-R000000";

try {
     response = webstoreClient.getRefund(refundId);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```

## Generate Button Signature (helper function)

This method is available in the base client ("AmazonPayClient").  This method does not invoke an API call, it is a helper function only.

The signatures generated by this helper function are only valid for the Checkout v2 front-end buttons.  Unlike API signing, no timestamps are involved, so the result of this function can be considered a static signature that can safely be placed in your website JS source files and used repeatedly (as long as your payload does not change).

```java
    String payload = "{\"storeId\":\"amzn1.application-oa2-client.xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\",\"webCheckoutDetails\":{\"checkoutReviewReturnUrl\":\"https://localhost/test/CheckoutReview.php\",\"checkoutResultReturnUrl\":\"https://localhost/test/CheckoutResult.php\"}}";
    String signature = client.generateButtonSignature(payload);
```

Or, if you want to use a JSONObject:

```java
        JSONObject payload = new JSONObject();
        payload.put("storeId", "amzn1.application-oa2-client.xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        JSONObject webCheckoutDetails = new JSONObject();
        webCheckoutDetails.put("checkoutReviewReturnUrl", "https://localhost/test/CheckoutReview.php");
        webCheckoutDetails.put("checkoutResultReturnUrl", "https://localhost/test/CheckoutResult.php");
        payload.put("webCheckoutDetails", webCheckoutDetails);

        String signature = client.generateButtonSignature(payload);
```

## In-Store API
Please contact your Amazon Pay Account Manager before using the In-Store API calls in a Production environment to obtain a copy of the In-Store Integration Guide.

### Making a merchantScan request

```java
JSONObject scanPayload = new JSONObject();
scanPayload.put("scanData", "UKhrmatMeKdlfY6b");
scanPayload.put("scanReferenceId", "0b8fb271-2ae2-49a5-b35d890");
scanPayload.put("merchantCOE", "DE");
scanPayload.put("ledgerCurrency", "EUR");

AmazonPayResponse response = null;
JSONObject scanResponse = null;

try {
    InstoreClient client = new InstoreClient(payConfiguration);
    response = instoreClient.merchantScan(scanPayload);
    scanResponse = response.getResponse();
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

String chargePermissionId = scanResponse.getString("chargePermissionId");
```

### Making a charge request

```java
JSONObject chargePayload = new JSONObject();
JSONObject chargeTotal = new JSONObject();
chargeTotal.put("currencyCode", "EUR");
chargeTotal.put("amount", 2);
chargePayload.put("chargeTotal", chargeTotal);
chargePayload.put("chargePermissionId", "S02-8295796-8107357");
chargePayload.put("chargeReferenceId", "chargeReferenceId-2");
chargePayload.put("softDescriptor", "amzn-store");

JSONObject chargeResponse = null;
try {
    chargeResponse = instoreClient.charge(scanPayload).getResponse();
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

String chargeId = chargeResponse.getString("chargeId");

```

### Making a merchantScan request with the auth token

```java
JSONObject scanPayload = new JSONObject();
scanPayload.put("scanData", "UKhrmatMeKdlfY6b");
scanPayload.put("scanReferenceId", "0b8fb271-2ae2-49a5-b35d890");
scanPayload.put("merchantCOE", "DE");
scanPayload.put("ledgerCurrency", "EUR");

JSONObject scanResponse = null;
try {
    scanResponse = instoreClient.merchantScan(scanPayload).getResponse();
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

String chargePermissionId = scanResponse.getString("chargePermissionId");
```

# Authorization Tokens API (Advanced Use-Cases Only)

Please note that your Solution Provider account must have a pre-existing relationship (valid and active API V1-style MWS authorization token) with the merchant account in order to use this function.

```java

AmazonPayClient client;
PayConfiguration payConfiguration;
AmazonPayResponse response;
String mwsAuthToken = /* the third part mws auth token */;
String merchantId = /* the third party merchant id */; 
String privateKey = /* your private key */;
String publicKeyId = /* your public key id */;
try {
    payConfiguration = new PayConfiguration();
    payConfiguration.setRegion(Region.EU). // select your region
            setEnvironment(Environment.LIVE). // must be live
            setPrivateKey(privateKey).
            setPublicKeyId(publicKeyId);

    client = new AmazonPayClient(payConfiguration);
    response = client.getAuthorizationToken(mwsAuthToken, merchantId, null);
    // If OK response.getStatus() shall be 200
    // If OK response.getResponse() provides the mwsAuthToken
} catch (Exception e) {
    // System.out.println(e.getMessage());
    /* handle Exception here */
}

```

# Manual Signing (Advanced Use-Cases Only)

This SDK provides the ability to help you manually sign your API requests if you want to use your own code for sending the HTTPS request over the Internet.

```java
JSONObject scanPayload = new JSONObject();
scanPayload.put("scanData", "UKhrmatMeKdlfY6b");
scanPayload.put("scanReferenceId", "0b8fb271-2ae2-49a5-b35d890");
scanPayload.put("merchantCOE", "DE");
scanPayload.put("ledgerCurrency", "EUR");

AmazonPayResponse response = null;
JSONObject scanResponse = null;

RequestSigner requestSigner = null;
try {
    requestSigner = new RequestSigner(payConfiguration);
} catch (AmazonPayClientException e) {
   e.printStackTrace();
}

URI scanUri = new URI("https://pay-api.amazon.com/sandbox/in-store/v1/merchantScan");

Map<String, List<String>> queryParametersMap = new HashMap<>();

Map<String, String> header = new HashMap<String, String>();

Map<String, String> postSignedHeaders = null;
try {
    postSignedHeaders = requestSigner.signRequest(scanUri, "POST", queryParametersMap, scanPayload.toString(), header);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

HttpURLConnection conn = (HttpURLConnection)scanUri.toURL().openConnection();

for (Map.Entry<String, String> entry : postSignedHeaders.entrySet()) {
    conn.setRequestProperty(entry.getKey(), entry.getValue());
}

conn.setDoOutput(true);
conn.setRequestMethod("POST");
OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
out.write(scanPayload.toString());
out.close();

int responseCode = conn.getResponseCode();

BufferedReader in;
if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
    in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
} else {
    in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
}
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine).append("\n");
}

String chargePermissionId = JSONObject.fromObject(response.toString()).getString("chargePermissionId");
```

#  Reporting APIs code samples

## Amazon Checkout v2 Reporting APIs - GetReports API
```java
AmazonPayResponse response = null;

Map<String, List<String>> queryParameters = new HashMap<>();
List<String> reportTypes = new ArrayList<>();
reportTypes.add("_GET_FLAT_FILE_OFFAMAZONPAYMENTS_SETTLEMENT_DATA_");
List<String> processingStatuses = new ArrayList<>();
processingStatuses.add("COMPLETED");

queryParameters.put("reportTypes", reportTypes);
queryParameters.put("processingStatuses", processingStatuses);

try {
     response = webstoreClient.getReports(queryParameters);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}
```

## Amazon Checkout v2 Reporting APIs - GetReportById API
```java
AmazonPayResponse response = null;
String reportId = "1234567890";

try {
     response = webstoreClient.getReportById(reportId);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}
```

## Amazon Checkout v2 Reporting APIs - GetReportDocument API
```java
AmazonPayResponse response = null;
String reportDocumentId = "amzn1.tortuga.0.000000000-0000-0000-0000-000000000000.00000000000000";

try {
     response = webstoreClient.getReportDocument(reportDocumentId);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}
```

## Amazon Checkout v2 Reporting APIs - GetReportSchedules API
```java
AmazonPayResponse response = null;
String reportTypes = "_GET_FLAT_FILE_OFFAMAZONPAYMENTS_ORDER_REFERENCE_DATA_,_GET_FLAT_FILE_OFFAMAZONPAYMENTS_BILLING_AGREEMENT_DATA_";

try {
     response = webstoreClient.getReportSchedules(reportTypes);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}
```

## Amazon Checkout v2 Reporting APIs - GetReportScheduleById API
```java
AmazonPayResponse response = null;
String reportScheduleId = "1234567890";

try {
     response = webstoreClient.getReportScheduleById(reportScheduleId);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}
```

## Amazon Checkout v2 Reporting APIs - CreateReport API
```java
AmazonPayResponse response = null;
JSONObject requestPayload = new JSONObject();
requestPayload.put("reportType", "_GET_FLAT_FILE_OFFAMAZONPAYMENTS_ORDER_REFERENCE_DATA_");
requestPayload.put("startTime", "20221114T074550Z");
requestPayload.put("endTime", "20221202T150350Z");
Map<String, String> header = new HashMap<String, String>();
header.put("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));

try {
     response = webstoreClient.createReport(requestPayload, header);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}
```

## Amazon Checkout v2 Reporting APIs - CreateReportSchedule API
```java
AmazonPayResponse response = null;
JSONObject requestPayload = new JSONObject();
requestPayload.put("reportType", "_GET_FLAT_FILE_OFFAMAZONPAYMENTS_ORDER_REFERENCE_DATA_");
requestPayload.put("scheduleFrequency", "P14D");
requestPayload.put("nextReportCreationTime", "20221202T150350Z");
requestPayload.put("deleteExistingSchedule", "false");
Map<String, String> header = new HashMap<String, String>();
header.put("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));

try {
     response = webstoreClient.createReportSchedule(requestPayload, header);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}
```

## Amazon Checkout v2 Reporting APIs - CancelReportSchedule API
```java
AmazonPayResponse response = null;
String reportScheduleId = "1234567890";

try {
     response = webstoreClient.cancelReportSchedule(reportScheduleId);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}
```

## Amazon Checkout v2 Reporting APIs - getDisbursements API
```java
final Map<String, List<String>> queryParameters = getDisbursementsQueryParameters();

final Map<String, String> header = Collections.singletonMap("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));

try {
    final AmazonPayResponse response = webstoreClient.getDisbursements(queryParameters, header);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

private static Map<String, List<String>> getDisbursementsQueryParameters() {

        final Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("startTime", Collections.singletonList("20240715T000000Z"));
        queryParameters.put("endTime", Collections.singletonList("20240801T235959Z"));
        queryParameters.put("pageSize", Collections.singletonList("5"));
        queryParameters.put("nextToken", Collections.singletonList(""));

        return queryParameters;
}
```

## AmazonPay Single Page Checkout APIs

### Making a finalizeCheckoutSession request

```java

AmazonPayResponse response = null;
JSONObject payload = new JSONObject();


JSONObject shippingAddressDetails = new JSONObject();
shippingAddressDetails.put("name", "Susie Smith");
shippingAddressDetails.put("addressLine1","10 Ditka Ave");
shippingAddressDetails.put("addressLine2","Suite 2500");
shippingAddressDetails.put("city","Chicago");
shippingAddressDetails.put("county",JSONObject.NULL);
shippingAddressDetails.put("district",JSONObject.NULL);
shippingAddressDetails.put("stateOrRegion","IL");
shippingAddressDetails.put("postalCode","60602");
shippingAddressDetails.put("countryCode","US");
shippingAddressDetails.put("phoneNumber","800-000-0000");
payload.put("shippingAddress", shippingAddressDetails);

JSONObject chargeAmountDetails = new JSONObject();
chargeAmountDetails.put("amount", "1");
chargeAmountDetails.put("currencyCode", "USD");
payload.put("chargeAmount", chargeAmountDetails);

payload.put("paymentIntent", "Confirm");
payload.put("canHandlePendingAuthorization","false");

Map<String, String> header = new HashMap<String, String>();
header.put("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));

try {
     response = webstoreClient.finalizeCheckoutSession(checkoutSessionId, payload, header);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}
```

## AmazonPay Dispute APIs for PSPs.

### CreateDispute API request
```java
final Map<String, String> header = Collections.singletonMap("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));

final JSONObject payload = createDisputePayload();

try {
    final AmazonPayResponse response =  webstoreClient.createDispute(payload, header);
    System.out.println("Response : " + response.toString());
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

// Create Dispute API Payload
private JSONObject createDisputePayload() throws JSONException {
    final String chargeId = "P03-0000000-0000000-C0000000";
    final String providerDisputeId = "psp_dispute_1234";
    final String amount = "1";
    final String currencyCode = "JPY";
    final DisputeFilingReason filingReason = DisputeFilingReason.PRODUCT_NOT_RECEIVED;
    final DisputeState state = DisputeState.ACTION_REQUIRED;
    final DisputeReasonCode reasonCode = DisputeReasonCode.MERCHANT_RESPONSE_REQUIRED;
    final String reasonDescription = "Merchant needs to provide a response";

    // Time the dispute was filed
    final long filingTimestamp = System.currentTimeMillis() / 1000L;
    // Time window by which merchant should respond to a dispute request, otherwise dispute will be resolved in buyer's favour.
    final long merchantResponseDeadline = filingTimestamp + (07 * 24 * 60 * 60);  // For 07 Days Time Period.

    return new JSONObject()
            .put("chargeId", chargeId)
            .put("providerMetadata", createProviderMetadata(providerDisputeId))
            .put("disputeAmount", createDisputeAmount(amount, currencyCode))
            .put("filingReason", filingReason.getDisputeFilingReason())
            .put("statusDetails", createStatusDetails(null, state, reasonCode, reasonDescription))
            .put("filingTimestamp", filingTimestamp)
            .put("merchantResponseDeadline", merchantResponseDeadline);
}

private JSONObject createProviderMetadata(String providerDisputeId) throws JSONException {
    final JSONObject providerMetadata = new JSONObject();
    providerMetadata.put("providerDisputeId", providerDisputeId);
    return providerMetadata;
}

private JSONObject createDisputeAmount(String amount, String currencyCode) throws JSONException {
    final JSONObject disputeAmount = new JSONObject();
    disputeAmount.put("amount", amount);
    disputeAmount.put("currencyCode", currencyCode);
    return disputeAmount;
}

private JSONObject createStatusDetails(DisputeResolution resolution, DisputeState state, DisputeReasonCode reasonCode, String reasonDescription) throws JSONException {
    final JSONObject statusDetails = new JSONObject();

    if (resolution != null) {
        statusDetails.put("resolution", resolution.getDisputeResolution());
    }
    if (state != null) {
        statusDetails.put("state", state.getDisputeState());
    }
    if (reasonCode != null) {
        statusDetails.put("reasonCode", reasonCode.getDisputeReasonCode());
    }
    if (reasonDescription != null && !reasonDescription.isEmpty()) {
        statusDetails.put("reasonDescription", reasonDescription);
    }

    return statusDetails;
}
```

### UpdateDispute API request
```java
final String disputeId = "P03-0000000-0000000-B0000000";
final JSONObject payload = updateDisputePayload();

try {
    final AmazonPayResponse response = webstoreClient.updateDispute(disputeId, payload);
    System.out.println("Response : " + response.toString());
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

// Update Dispute API Payload
private JSONObject updateDisputePayload() throws JSONException {
    final DisputeState state = DisputeState.CLOSED;
    final DisputeResolution resolution = DisputeResolution.MERCHANT_WON;
    final DisputeReasonCode reasonCode = DisputeReasonCode.CHARGEBACK_FILED;
    final String reasonDescription = "Buyer has filed chargeback besides Claim dispute";

    // Current Unix timestamp (seconds since epoch)
    final long closureTimestamp = System.currentTimeMillis() / 1000L;

    return new JSONObject()
            .put("statusDetails", createStatusDetails(resolution, state, reasonCode, reasonDescription))
            .put("closureTimestamp", closureTimestamp);
}

private JSONObject createStatusDetails(DisputeResolution resolution, DisputeState state, DisputeReasonCode reasonCode, String reasonDescription) throws JSONException {
    final JSONObject statusDetails = new JSONObject();

    if (resolution != null) {
        statusDetails.put("resolution", resolution.getDisputeResolution());
    }
    if (state != null) {
        statusDetails.put("state", state.getDisputeState());
    }
    if (reasonCode != null) {
        statusDetails.put("reasonCode", reasonCode.getDisputeReasonCode());
    }
    if (reasonDescription != null && !reasonDescription.isEmpty()) {
        statusDetails.put("reasonDescription", reasonDescription);
    }

    return statusDetails;
}
```

### ContestDispute API request
```java
final Map<String, String> header = Collections.singletonMap("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));
final String disputeId = "P03-0000000-0000000-B0000000";
final JSONObject payload = contestDisputePayload();

try {
    final AmazonPayResponse response = webstoreClient.contestDispute(disputeId, payload, header);
    System.out.println("Response : " + response.toString());
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

// Contest Dispute API Payload
private JSONObject contestDisputePayload() throws JSONException {
    // Evidence 1
    final EvidenceType evidenceType1 = EvidenceType.TRACKING_NUMBER;
    final String fileId1 = null;
    final String evidenceText1 = "raw text supporting merchant evidence";
    // Evidence 2
    final EvidenceType evidenceType2 = EvidenceType.CUSTOMER_SIGNATURE;
    final String fileId2 = "customer_signature_file_id";
    final String evidenceText2 = null;

    return new JSONObject()
            .append("merchantEvidences", createMerchantEvidence(evidenceType1, fileId1, evidenceText1))
            .append("merchantEvidences", createMerchantEvidence(evidenceType2, fileId2, evidenceText2));
}

private JSONObject createMerchantEvidence(EvidenceType evidenceType, String fileId, String evidenceText) throws JSONException {
    final JSONObject evidence = new JSONObject();
    evidence.put("evidenceType", evidenceType.getEvidenceType());
    evidence.put("fileId", fileId);
    evidence.put("evidenceText", evidenceText);
    return evidence;
}
```

### UploadFile API request
```java
final Map<String, String> header = Collections.singletonMap("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));

final JSONObject payload = uploadFilePayload();

try {
    final AmazonPayResponse response =  webstoreClient.uploadFile(payload, header);
    System.out.println("Response : " + response.toString());
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

// Upload File API Payload
private JSONObject uploadFilePayload() throws JSONException {
    final EvidenceDocumentFileType documentFileType = EvidenceDocumentFileType.JPG;
    final DisputeFilePurpose disputeFilePurpose = DisputeFilePurpose.DISPUTE_EVIDENCE;

    return new JSONObject()
            .put("type", documentFileType.getEvidenceDocumentFileType())
            .put("purpose", disputeFilePurpose.getDisputeFilePurpose());
}
```

### CreateMerchantAccount API Request

```java
final AccountManagementClient client = new AccountManagementClient(payConfiguration);
final JSONObject createMerchantAccountPayload = getCreateMerchantAccountAPI();
try {
    final AmazonPayResponse response = client.createMerchantAccount(createMerchantAccountPayload, new HashMap<>());
    System.out.println(response.getRawResponse());
} catch (AmazonPayClientException e){
    e.printStackTrace();
}

private static JSONObject getCreateMerchantAccountAPI() throws JSONException {
    JSONObject createMerchantAccountPayload = new JSONObject();

    createMerchantAccountPayload.put("uniqueReferenceId", "Hanabi" + UUID.randomUUID());
    createMerchantAccountPayload.put("ledgerCurrency", "JPY");

    // Business Info
    JSONObject businessInfo = new JSONObject();
    businessInfo.put("email", "rufus" + UUID.randomUUID() + "@abc.com");
    businessInfo.put("businessType", "CORPORATE");
    businessInfo.put("businessLegalName", "密林コーヒー");
    businessInfo.put("businessCategory", "Beauty");

    // Business Address
    JSONObject businessAddress = new JSONObject();
    businessAddress.put("addressLine1", "扇町４丁目５－");
    businessAddress.put("addressLine2", "フルフィルメントセンタービル");
    businessAddress.put("city", "小田原市");
    businessAddress.put("stateOrRegion", "神奈川県");
    businessAddress.put("postalCode", "250-0001");
    businessAddress.put("countryCode", "JP");

    // Phone Number
    JSONObject phoneNumber = new JSONObject();
    phoneNumber.put("countryCode", "81");
    phoneNumber.put("number", "2062062061");

    businessAddress.put("phoneNumber", phoneNumber);
    businessInfo.put("businessAddress", businessAddress);

    // Business Display Name & Annual Sales Volume
    businessInfo.put("businessDisplayName", "Rufus's Cafe");

    JSONObject annualSalesVolume = new JSONObject();
    annualSalesVolume.put("amount", "100000");
    annualSalesVolume.put("currencyCode", "JPY");
    businessInfo.put("annualSalesVolume", annualSalesVolume);

    businessInfo.put("countryOfEstablishment", "JP");

    // Customer Support Info
    JSONObject customerSupportInformation = new JSONObject();
    customerSupportInformation.put("customerSupportEmail", "test.merchant" + UUID.randomUUID() + "@abc.com");

    JSONObject customerSupportPhoneNumber = new JSONObject();
    customerSupportPhoneNumber.put("countryCode", "1");
    customerSupportPhoneNumber.put("number", "1234567");
    customerSupportPhoneNumber.put("extension", "123");

    customerSupportInformation.put("customerSupportPhoneNumber", customerSupportPhoneNumber);
    businessInfo.put("customerSupportInformation", customerSupportInformation);

    createMerchantAccountPayload.put("businessInfo", businessInfo);

    // Beneficiary Owners
    JSONArray beneficiaryOwners = new JSONArray();

    JSONObject owner1 = new JSONObject();
    owner1.put("personId", "BO1");
    owner1.put("personFullName", "Rufus Rufus");
    owner1.put("residentialAddress", businessAddress); // Reuse the same address

    beneficiaryOwners.put(owner1);

    createMerchantAccountPayload.put("beneficiaryOwners", beneficiaryOwners);

    // Primary Contact Person
    JSONObject primaryContactPerson = new JSONObject();
    primaryContactPerson.put("personFullName", "Rufus Rufus");
    createMerchantAccountPayload.put("primaryContactPerson", primaryContactPerson);

    // Integration Info
    JSONObject integrationInfo = new JSONObject();
    JSONArray ipnEndpointUrls = new JSONArray();
    ipnEndpointUrls.put("https://cloudfront.net/ipnendpoint");
    ipnEndpointUrls.put("https://cloudfront.net/ipnendpoint");
    integrationInfo.put("ipnEndpointUrls", ipnEndpointUrls);
    createMerchantAccountPayload.put("integrationInfo", integrationInfo);

    JSONArray stores = new JSONArray();

    // Create a store object
    JSONObject store = new JSONObject();

    // Mandatory: Domain URLs
    JSONArray domainUrls = new JSONArray();
    domainUrls.put("https://www.rufus.com");
    store.put("domainUrls", domainUrls);

    // Optional: Store Name & Privacy Policy URL
    store.put("storeName", "Rufus's Cafe");
    store.put("privacyPolicyUrl", "http://www.rufus.com/privacy");

    // Optional: Store Status
    JSONObject storeStatus = new JSONObject();
    storeStatus.put("state", "Active"); // ENUM type

    // Remove reasonCode if not required
    // storeStatus.put("reasonCode", JSONObject.NULL);

    store.put("storeStatus", storeStatus);

    // Add the store object to the stores array
    stores.put(store);

    // Add the stores array to the payload
    createMerchantAccountPayload.put("stores", stores);

    // Merchant Status
    JSONObject merchantStatus = new JSONObject();
    merchantStatus.put("statusProvider", "Ayden");
    merchantStatus.put("state", "ACTIVE");
    merchantStatus.put("reasonCode", JSONObject.NULL);
    createMerchantAccountPayload.put("merchantStatus", merchantStatus);

    // Print the final JSON
    System.out.println(createMerchantAccountPayload.toString(4)); // Pretty print JSON

    return createMerchantAccountPayload;
}
```

### UpdateMerchantAccount API Request

```java
final JSONObject updateMerchantAccountPayload = getUpdateMerchantAccountAPI();
final Map<String, String> header = new HashMap<>();
header.put("x-amz-pay-authToken", "AUTH_TOKEN");
try {
    final AmazonPayResponse response = client.updateMerchantAccount("AXXXXXXX", updateMerchantAccountPayload, header);
    System.out.println(response.getRawResponse());
} catch (AmazonPayClientException e){
    e.printStackTrace();
}

private static JSONObject getUpdateMerchantAccountAPI() throws JSONException {
    JSONObject businessInfo = new JSONObject();
    JSONObject businessAddress = new JSONObject();
    JSONObject phoneNumber = new JSONObject();

    phoneNumber.put("countryCode", "81");
    phoneNumber.put("number", "2062062061");

    businessAddress.put("addressLine1", "扇町４丁目５－");
    businessAddress.put("addressLine2", "フルフィルメントセンタービル");
    businessAddress.put("city", "小田原市");
    businessAddress.put("stateOrRegion", "神奈川県");
    businessAddress.put("postalCode", "250-0001");
    businessAddress.put("countryCode", "JP");
    businessAddress.put("phoneNumber", phoneNumber);

    businessInfo.put("businessAddress", businessAddress);

    JSONObject payload = new JSONObject();
    payload.put("businessInfo", businessInfo);

    return payload;
}
```

### MerchantAccountClaim API Request

```java
final JSONObject payload = new JSONObject();
payload.put("uniqueReferenceId", "xxxxxxx-xxxx-xxxx-xxxx-xxxxxx");
try {
    final AmazonPayResponse response = client.merchantAccountClaim("AXXXXXXX", payload, new HashMap<>());
    System.out.println(response.getRawResponse());
} catch (AmazonPayClientException e){
    e.printStackTrace();
}
```