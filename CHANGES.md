### Version 2.6.6 - February 2025
* Introducing new Account Management APIs that allow partners to programmatically onboard merchants onto the Amazon Pay.
* The `createMerchantAccount` - Provide merchant info through this API to create loginable account for your merchant partners. Client should expect either a success message or a detailed error message based on data validation and fulfillment.
* The `updateMerchantAccount` - Updates a merchant account and store for the given Amazon merchantAccountId. Partners are only able to update fields which do not change the legal business entity itself.
* The `merchantAccountClaim` - Initiates the merchant account claim process. Clients should expect a redirection response or a detailed error message based on data validation and fulfillment.

### Version 2.6.5 - January 2025
* Introducing new v2 Dispute APIs for PSPs. Buyers can create a dispute by filing an Amazon Pay A-to-z Guarantee claim or by filing a chargeback with their bank.
* The `createDispute` API is used to notify Amazon of a newly created chargeback dispute by a buyer on a transaction processed by the PSP (Payment Service Provider), ensuring the dispute is properly accounted for in the Amazon Pay systems.
* The `updateDispute` API is used to notify Amazon of the closure status of a chargeback dispute initiated by a buyer for orders processed by a partner PSP (Payment Service Provider), ensuring proper accounting within the Amazon systems.
* The `contestDispute` API is used by the partner, on behalf of the merchant, to formally contest a dispute managed by Amazon, requiring the submission of necessary evidence files within the specified Dispute Window (11 days for Chargeback, 7 days for A-Z Claims).
* The `uploadFile` API is utilised by PSPs (Payment Service Provider) to upload file-based evidence when a merchant contests a dispute, providing the necessary reference ID to the evidence file as part of the Update Dispute API process.

### Version 2.6.4 - November 2024
* Introducing the updateCharge API.
* The `updateCharge` API enables you to update the charge status of any PSP (Payment Service Provider) processed payment method (PPM) transactions.

### Version 2.6.3 - August 2024
* Introducing the getDisbursements API.
* The `getDisbursements` API enables you to retrieve disbursement details based on a specified date range for settlement dates.

### Version 2.6.2 - January 2024
* Setting UTF-8 as default encoding format during Signature generation.
* Introducing new API called finalizeCheckoutSession which validates critical attributes in merchantMetadata then processes payment. Use this API to process payments for JavaScript-based integrations.
* Corrected README.md file related to finalizeCheckoutSession API and Reporting APIs.

### Version 2.6.1 - November 2023
* Introducing new Merchant Onboarding & Account Management APIs, which allows our partners to onboard merchants programatically and as part of account management offer them creation, updation and deletion/dissociation capability.
* Fixed connection pooling issue to enhance stability and performance.
* Corrected README.md file.
* Enable client request to configure connection, connect and read timeout.
* Fixed Security risk

### Version 2.6.0 - March 2023
* Introducing new v2 Reporting APIs. Reports allow you to retrieve consolidated data about Amazon Pay transactions and settlements. In addition to managing and downloading reports using Seller Central, Amazon Pay offers APIs to manage and retrieve your reports.
* Introducing new signature generation algorithm AMZN-PAY-RSASSA-PSS-V2 & increasing salt length from 20 to 32.
* Added support for handling new parameter 'shippingAddressList' in Checkout Session response. Change is fully backwards compatible.
* Enabled Connection Pooling Support.
* Adding Error code 408 to API retry logic
* Note : To use new algorithm AMZN-PAY-RSASSA-PSS-V2, "algorithm" needs to be provided as an additional field in "payConfiguration" and also while rendering Amazon Pay button in "createCheckoutSessionConfig". The changes are backwards-compatible, SDK will use AMZN-PAY-RSASSA-PSS by default.

#### Version 2.5.1 - January 2022
* Applied patch to address issues occurred in Version 2.5.0.
* **Please dont use Version 2.5.0**

#### Version 2.5.0 - January 2022
* Migrated signature generating algorithm from AMZN-PAY-RSASSA-PSS to AMZN-PAY-RSASSA-PSS-V2 & increased salt length from 20 to 32
* Note : From this SDK version, "algorithm" need to be provided as additional field in "createCheckoutSessionConfig" while rendering Amazon Pay button.

#### Version 2.4.0 - October 2021
* Removed library "net.sf.json" & added library "org.json" as alternative for handling JSON in order to address security issues & to improve performance
* Note: 
* 1. Consumers of previous SDK versions must start using import as 'org.json' instead of 'net.sf.json' as of this SDK Version 2.4.0.
* 2. In order to add data to JSONArray, you need to use 'put' method instead of 'add' method as of this SDK Version 2.4.0.

#### Version 2.3.4 - September 2021
* Removed parameter named "overrideServiceURL" from PayConfiguration

#### Version 2.3.3 - June 2021
* Added API Retry mechanism for error codes 502 & 504
* Upgraded Bouncy Castle to avoid using Vulnerable version
  		 
#### Version 2.3.2 - May 2021
* Enabled support for environment specific keys (i.e Public key & Private key). The changes are fully backwards-compatible, where merchants can also use non environment specific keys 

#### Version 2.3.1 - April 2021
* Enabled character encoding UTF-8 when converting the body/payload to a string entity before sending HTTP/HTTPS request

#### Version 2.3.0 - March 2021
* Introduced Apache HTTPClient library for HTTPS/TCP communications which will allow SDK to work with latest versions of java
* Fixed Security risk
* Enabled Proxy Support
* Note: Consumers of previous SDK versions must update data type of private key from string to char[] as of this SDK Version 2.3.0.

#### Version 2.2.2 - July 2020

* Fix issue causing SDK to fail for Java 12 and higher
* Restructure README.md file

* Underlying endpoint for getBuyer API changed

#### Version 2.2.1 - June 2020

* Underlying endpoint for getBuyer API changed

#### Version 2.2.0 - June 2020

* Added getBuyer() API call

#### Version 2.1.0 - May 2020

* New convenience method: AmazonPayClient.generateButtonSignature() to assist the developer in generating static signatures for the createCheckoutSessionConfig.payloadJSON attribute that can be used by Checkout v2's amazon.Pay.renderButton method
* Added toString() override in AmazonPayResponse to make it easier to get troubleshooting information out of a request

#### Version 2.0.0 - April 2020
	  
SDK changes to support Amazon Pay API version 2:
	 
* Introduction of "Complete CheckoutSession" API call
* Bouncy Castle library update
	  
#### Version 1.0.0 - April 2020
	  
Initial release, including support for
	 
* Amazon Pay Checkout v2

