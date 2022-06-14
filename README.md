# SOAP Client with Liferay 7.4

This workspace has a module that access a public SOAP webservice.

`http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL`

On Liferay 7.4, using JDK8, it should work fine.
Using JDK11, it breaks with the following issue:

`Error: Could not find wsdl:binding operation info for web method listOfCountryNamesByName.
`

### How to test
- Run `gw initBundle`
- Start the portal
- Run `callCountries` on Gogo Shell