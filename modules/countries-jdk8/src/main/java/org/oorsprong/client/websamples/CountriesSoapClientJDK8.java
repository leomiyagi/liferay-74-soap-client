package org.oorsprong.client.websamples;

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;

import javax.xml.ws.BindingProvider;
import java.net.MalformedURLException;
import java.net.URL;

@Component(immediate = true,
        property = {
                "osgi.command.function=callCountriesJDK8", "osgi.command.scope=blade"
        },
        service = CountriesSoapClientJDK8.class)
public class CountriesSoapClientJDK8 {

    public void callCountriesJDK8() {
        CountryInfoServiceSoapType service = _getService();

        ArrayOftCountryCodeAndName arrayOftCountryCodeAndName = service.listOfCountryNamesByName();

        arrayOftCountryCodeAndName.getTCountryCodeAndName().stream().forEach(tCountryCodeAndName -> System.out.println(tCountryCodeAndName.getSName()));
    }

    private CountryInfoServiceSoapType _getService() {

        URL wsdlLocation = null;

        String wsdl = "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL";
        try {
            wsdlLocation = new URL(wsdl);
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        CountryInfoService infoService = new CountryInfoService(wsdlLocation);
        CountryInfoServiceSoapType countryInfoServiceSoap = infoService.getCountryInfoServiceSoap();

        ((BindingProvider)countryInfoServiceSoap).
                getRequestContext(
                ).put(
                        BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                        wsdl
                );

        return countryInfoServiceSoap;
    }

    private Bundle _bundle;
}
