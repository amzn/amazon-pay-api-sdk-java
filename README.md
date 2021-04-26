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
        <version>2.3.1</version>
    </dependency>
</dependencies>
```

To use the SDK in a Gradle project, add the following line to your build.gradle file::

```
implementation 'software.amazon.pay:amazon-pay-api-sdk-java:2.3.1'
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
openssl rsa -in private.txt -pubout > public.txt
```

The first command above generates a private key and the second line uses the private key to generate a public key.

To associate the key with your account, follow the instructions here to
[Get your Public Key ID](http://amazonpaycheckoutintegrationguide.s3.amazonaws.com/amazon-pay-checkout/get-set-up-for-integration.html#4-get-your-public-key-id).

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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
                .setEnvironment(Environment.SANDBOX);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

// If you have your private key in a file, you can set it in the payConfiguration in the following way:

try {
    payConfiguration = new PayConfiguration()
                   .setPublicKeyId("YOUR_PUBLIC_KEY_ID")
                   .setRegion(Region.YOUR_REGION_CODE)
                   .setPrivateKey(new String(Files.readAllBytes(Paths.get("private.pem"))).toCharArray())
                   .setEnvironment(Environment.SANDBOX);
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
                .setEnvironment(Environment.SANDBOX);
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
                   .setProxySettings(proxySettings);
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

* AmazonPayClient: **deliveryTracker**(JSONObject payload[, Map<String, String> header]) &#8594; POST to "$version/deliveryTrackers"

## Amazon Checkout v2 API
[Amazon Pay Checkout v2 Integration Guide](https://amazonpaycheckoutintegrationguide.s3.amazonaws.com/amazon-pay-api-v2/introduction.html)

### Amazon Checkout v2 Buyer object
* WebstoreClient: **getBuyer**(String buyerToken[, Map<String, String> header]) &#8594; GET to "$version/buyers/$buyerToken"

### Amazon Checkout v2 CheckoutSession object
* WebstoreClient: **createCheckoutSession**(JSONObject payload, Map<String, String> header) &#8594; POST to "$version/checkoutSessions"
* WebstoreClient: **getCheckoutSession**(String checkoutSessionId[, Map<String, String> header]) &#8594; GET to "$version/checkoutSessions/$checkoutSessionId"
* WebstoreClient: **updateCheckoutSession**(String checkoutSessionId, JSONObject payload[, Map<String, String> header]) &#8594; PATCH to "$version/checkoutSessions/$checkoutSessionId"
* WebstoreClient: **completeCheckoutSession**(String checkoutSessionId, JSONObject payload[, Map<String, String> header]) &#8594; POST to "$version/checkoutSessions/$checkoutSessionId/complete"

### Amazon Checkout v2 ChargePermission object
* WebstoreClient: **getChargePermission**(String chargePermissionId[, Map<String, String> header]) &#8594; GET to "$version/chargePermissions/$chargePermissionId"
* WebstoreClient: **updateChargePermission**(String chargePermissionId, JSONObject payload[, Map<String, String> header]) &#8594; PATCH to "$version/chargePermissions/$chargePermissionId"
* WebstoreClient: **closeChargePermission**(String chargePermissionId, JSONObject payload[, Map<String, String> header]) &#8594; DELETE to "$version/chargePermissions/$chargePermissionId/close"

### Amazon Checkout v2 Charge object
* WebstoreClient: **createCharge**(payload, Map<String, String> header) &#8594; POST to "$version/charges"
* WebstoreClient: **getCharge**(String chargeId[, Map<String, String> header]) &#8594; GET to "$version/charges/$chargeId"
* WebstoreClient: **captureCharge**(String chargeId, JSONObject payload[, Map<String, String> header]) &#8594; POST to "$version/charges/$chargeId/capture"
* WebstoreClient: **cancelCharge**(String chargeId, JSONObject payload[, Map<String, String> header]) &#8594; DELETE to "$version/charges/$chargeId/cancel"

### Amazon Checkout v2 Refund object
* WebstoreClient: **createRefund**(payload, Map<String, String> header) &#8594; POST to "$version/refunds"
* WebstoreClient: **getRefund**(String refundId[, Map<String, String> header]) &#8594; GET to "$version/refunds/$refundId"

## In-Store API
Please contact your Amazon Pay Account Manager before using the In-Store API calls in a Production environment to obtain a copy of the In-Store Integration Guide.

* InstoreClient: **merchantScan**(JSONObject scanRequest[, Map<String, String> header]) &#8594; POST to "$version/in-store/merchantScan"
* InstoreClient: **charge**(JSONObject chargeRequest[, Map<String, String> header]) &#8594; POST to "$version/in-store/charge"
* InstoreClient: **refund**(JSONObject refundRequest[, Map<String, String> header]) &#8594; POST to "$version/in-store/refund"

## Authorization Tokens API
Please note that your solution provider account must have a pre-existing relationship (valid and active MWS authorization token) with the merchant account in order to use this function.

* AmazonPayClient: **getAuthorizationToken**(String mwsAuthToken, String merchantId[, Map<String, String> header]) &#8594; GET to "$version/authorizationTokens/$mwsAuthToken?merchantId=$merchantId"

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
   * '**getResponse()*' - the response serialized into a JSONObject
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
deliveryDetailsArray.add(deliveryDetails);
payload.put("amazonOrderReferenceId", "P01-8845762-6072995");
payload.put("deliveryDetails", deliveryDetailsArray);

try {
     response = client.deliveryTracker(payload);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```
## Amazon Pay Checkout v2 API
[Checkout v2 Integration Guide](https://amazonpaycheckoutintegrationguide.s3.amazonaws.com/amazon-pay-api-v2/introduction.html)

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
payload.put("storeId", "amzn1.application-oa2-client.4c46698afa4d4b23b645d05762fc78fa");
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

try {
     response = webstoreClient.getCheckoutSession(checkoutSessionId);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```

### Making an updateCheckoutSession request

```java

AmazonPayResponse response = null;
JSONObject payload = new JSONObject();
JSONObject updateWebCheckoutDetails = new JSONObject();
updateWebCheckoutDetails.put("checkoutResultReturnUrl", "https://localhost/store/checkout_return");
payload.put("webCheckoutDetails", updateWebCheckoutDetails);

JSONObject paymentDetail = new JSONObject();
paymentDetail.put("paymentIntent" , "Authorize");
paymentDetail.put("canHandlePendingAuthorization", false);
JSONObject chargeAmount = new JSONObject();
chargeAmount.put("amount", "12.34");
chargeAmount.put("currencyCode", "USD");
paymentDetail.put("chargeAmount", chargeAmount);
payload.put("paymentDetail", paymentDetail);

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
JSONObject payload = new JSONObject();

JSONObject paymentDetail = new JSONObject();
JSONObject chargeAmount = new JSONObject();
chargeAmount.put("amount", "12.34");
chargeAmount.put("currencyCode", "USD");
paymentDetail.put("chargeAmount", chargeAmount);
payload.put("paymentDetail", paymentDetail);

try {
     response = webstoreClient.completeCheckoutSession(checkoutSessionId, payload);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```

### Making an getChargePermissions request

```java

AmazonPayResponse response = null;

try {
     response = webstoreClient.getChargePermissions(chargePermissionId);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```

### Making a updateChargePermission request

```java

AmazonPayResponse response = null;
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

try {
     response = webstoreClient.getCharge(chargesId);
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

payload.put("chargePermissionId", "S01-3152594-4330637");
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

### Making a captureCharge request

```java

AmazonPayResponse response = null;

JSONObject payload = new JSONObject();
JSONObject captureAmount = new JSONObject();
captureAmount.put("amount", "1.23");
captureAmount.put("currencyCode", "USD");
payload.put("captureAmount", captureAmount);
payload.put("softDescriptor", "My Soft Descriptor");

Map<String, String> header = new HashMap<String, String>();
header.put("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));

try {
     response = webstoreClient.captureCharge(chargesId, payload, header);
} catch (AmazonPayClientException e) {
    e.printStackTrace();
}

```

### Making a cancelCharge request

```java

AmazonPayResponse response = null;

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
