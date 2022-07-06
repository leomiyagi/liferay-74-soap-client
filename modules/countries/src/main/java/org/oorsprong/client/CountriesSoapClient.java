package org.oorsprong.client;

import org.oorsprong.websamples.ArrayOftCountryCodeAndName;
import org.oorsprong.websamples_countryinfo.CountryInfoService;
import org.oorsprong.websamples_countryinfo.CountryInfoServiceSoapType;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Component(immediate = true,
        property = {
                "osgi.command.function=callCountries", "osgi.command.scope=blade"
        },
        service = CountriesSoapClient.class)
public class CountriesSoapClient {

    @Activate
    @Modified
    public void activate(BundleContext bundleContext, Map<String, Object> properties) {

        _bundle = bundleContext.getBundle(0);
    }

    public void callCountries() {
        CountryInfoServiceSoapType service = _getService();

        ArrayOftCountryCodeAndName arrayOftCountryCodeAndName = service.listOfCountryNamesByName();

        arrayOftCountryCodeAndName.getTCountryCodeAndName().stream().forEach(tCountryCodeAndName -> System.out.println(tCountryCodeAndName.getSName()));
    }

    private CountryInfoServiceSoapType _getService() {
        ClassLoader classLoader = Endpoint.class.getClassLoader();
        Thread thread = Thread.currentThread();
        ClassLoader contextClassLoader = thread.getContextClassLoader();
        thread.setContextClassLoader(classLoader);

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

        thread.setContextClassLoader(contextClassLoader);

        return countryInfoServiceSoap;
    }

    private Bundle _bundle;
}
