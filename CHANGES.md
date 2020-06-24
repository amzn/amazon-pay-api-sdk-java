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

