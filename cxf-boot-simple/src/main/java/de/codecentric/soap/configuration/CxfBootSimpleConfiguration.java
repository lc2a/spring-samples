package de.codecentric.soap.configuration;

import javax.xml.ws.Endpoint;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.codecentric.cxf.xmlvalidation.CustomFaultBuilder;
import de.codecentric.namespace.weatherservice.Weather;
import de.codecentric.namespace.weatherservice.WeatherService;
import de.codecentric.soap.endpoint.WeatherServiceEndpoint;
import de.codecentric.soap.xmlvalidation.WeatherFaultBuilder;

@Configuration
public class CxfBootSimpleConfiguration {

    public static final String PUBLISH_URL_ENDING = "/WeatherService_1.0";
    
    @Autowired
    private SpringBus springBus;
    
    @Bean
    public WeatherService weatherService() {
        return new WeatherServiceEndpoint();
    }
    
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus, weatherService());        
        // CXF JAX-WS implementation relies on the correct ServiceName as QName-Object with
        // the name-Attribute´s text <wsdl:service name="Weather"> and the targetNamespace
        // "http://www.codecentric.de/namespace/weatherservice/"
        // Also the WSDLLocation must be set
        endpoint.setServiceName(weather().getServiceName());
        endpoint.setWsdlLocation(weather().getWSDLDocumentLocation().toString());
        endpoint.publish(PUBLISH_URL_ENDING);
        return endpoint;
    }
    
    @Bean
    public Weather weather() {
        // Needed for correct ServiceName & WSDLLocation to publish contract first incl. original WSDL
        return new Weather();
    }
    
    // Activating XML-Schema validation with custom Fault
    @Bean
    public CustomFaultBuilder weatherFaultBuilder() {
        return new WeatherFaultBuilder();
    }

}
