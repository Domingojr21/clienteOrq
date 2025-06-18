package com.banreservas.integration.util;

import org.apache.camel.CamelContext;
import org.apache.camel.component.cxf.common.DataFormat;
import org.apache.camel.component.cxf.jaxws.CxfEndpoint;
import org.eclipse.microprofile.config.inject.ConfigProperty;
// import org.tempuri.BackEndServiceSoap;
import org.tempuri.BackEndServiceSoap;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

/**
 * Configura y expone un bean {@link CxfEndpoint} para ser utilizado como
 * endpoint SOAP en Apache Camel.
 * <p>
 * Este bean configura el endpoint con parámetros definidos en el archivo de
 * configuración y
 * lo registra con un nombre específico para ser utilizado en rutas Camel
 * mediante {@code from("cxf:bean:banreservasClientEndpoint")}.
 * </p>
 * 
 * <p>
 * Se espera que el archivo WSDL se encuentre en el classpath.
 * </p>
 */
@ApplicationScoped
public class CxfConfig {

    /**
     * Dirección relativa donde se expondrá el servicio SOAP.
     * Ejemplo: {@code /generalNoticesSearch}
     */
    @ConfigProperty(name = "banreservas.client.service.soap.address")
    String addressService;

    /**
     * Indica si se habilita o no el logging de las solicitudes/respuestas SOAP.
     * Debe ser un valor booleano: {@code true} o {@code false}.
     */
    @ConfigProperty(name = "banreservas.client.service.soap.logging")
    String loggingService;

    /**
     * URL pública del servicio que será publicada en el WSDL.
     */
    @ConfigProperty(name = "banreservas.client.service.soap.endpoint")
    String endpointService;

    /**
     * Crea y configura un {@link CxfEndpoint} para el servicio SOAP, utilizando
     * la clase generada desde el WSDL como interfaz del servicio.
     *
     * @param camelContext El contexto de Camel inyectado por Quarkus.
     * @return Un {@link CxfEndpoint} configurado y listo para ser utilizado.
     */
    @Produces
    @Named("banreservasClientEndpoint")
    public CxfEndpoint consultBanreservasClientEndpoint(CamelContext camelContext) {
        CxfEndpoint endpoint = new CxfEndpoint();
        endpoint.setCamelContext(camelContext);
        endpoint.setAddress(addressService);
        endpoint.setPublishedEndpointUrl(endpointService);
        endpoint.setServiceClass(BackEndServiceSoap.class); // Interfaz generada desde el WSDL
        endpoint.setDataFormat(DataFormat.PAYLOAD); // Usar formato crudo (XML) para más control
        endpoint.setLoggingFeatureEnabled(Boolean.parseBoolean(loggingService));
        endpoint.setWsdlURL("classpath:wsdl/ClienteBanreservas.wsdl");
        return endpoint;
    }
}
