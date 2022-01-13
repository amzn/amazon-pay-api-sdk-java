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

