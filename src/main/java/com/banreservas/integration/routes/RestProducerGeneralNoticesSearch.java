// package com.banreservas.integration.routes;

// import org.apache.camel.Exchange;
// import org.apache.camel.LoggingLevel;
// import org.apache.camel.builder.RouteBuilder;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import jakarta.enterprise.context.ApplicationScoped;

// /**
//  * Producer para el servicio backend de búsqueda general de avisos MICM.
//  * Maneja las consultas al microservicio de avisos.
//  */
// @ApplicationScoped
// public class RestProducerGeneralNoticesSearch extends RouteBuilder {
    
//     private static final Logger logger = LoggerFactory.getLogger(RestProducerGeneralNoticesSearch.class);
    
//     @Override
//     public void configure() throws Exception {
        
//         from("direct:backend-notices-service")
//             .routeId("backend-notices-service-route")
//             .log(LoggingLevel.INFO, logger, "Iniciando consulta al servicio de búsqueda de avisos MICM")
            
//             .removeHeaders("CamelHttp*")
//             .removeHeader("host")
            
//             .setHeader(Exchange.HTTP_METHOD, constant("POST"))
//             .setHeader("Content-Type", constant("application/json"))
//             .setHeader("Accept", constant("application/json"))
            
//             .setHeader("id_consumidor", constant("MICM"))  
//             .setHeader("usuario", simple("${exchangeProperty.usuarioRq}"))
//             .setHeader("terminal", simple("${exchangeProperty.terminalRq}")) 
//             .setHeader("fecha_hora", simple("${date:now:yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX}")) 
//             .setHeader("operacion", constant("BusquedaGeneralAvisosMICM"))
            
//             .choice()
//                 .when(simple("${exchangeProperty.sessionIdHeader} != null"))
//                     .setHeader("sessionId", simple("${exchangeProperty.sessionIdHeader}"))
//                 .otherwise()
//                     .setHeader("sessionId", constant("ehbdeld54")) 
//             .end()
            
//             .setHeader("Authorization", simple("${header.Authorization}"))
            
//             .log(LoggingLevel.INFO, logger, "Usando Authorization header original: ${header.Authorization}")
            
//             .toD("{{backend.notices.service.url}}?bridgeEndpoint=true&throwExceptionOnFailure=false&connectTimeout={{backend.service.timeout}}&connectionRequestTimeout={{backend.service.timeout}}")
            
//             .choice()
//                 .when(header("CamelHttpResponseCode").isEqualTo(200))
//                     .log(LoggingLevel.INFO, logger, "Consulta de avisos MICM exitosa")
//                 .when(header("CamelHttpResponseCode").isEqualTo(400))
//                     .log(LoggingLevel.ERROR, logger, "Request inválido para servicio de avisos MICM")
//                     .throwException(new RuntimeException("Request inválido para búsqueda de avisos"))
//                 .when(header("CamelHttpResponseCode").isEqualTo(401))
//                     .log(LoggingLevel.ERROR, logger, "Token inválido para servicio de avisos MICM")
//                     .throwException(new RuntimeException("Token inválido para búsqueda de avisos"))
//                 .when(header("CamelHttpResponseCode").isEqualTo(404))
//                     .log(LoggingLevel.ERROR, logger, "Servicio de avisos MICM no encontrado")
//                     .throwException(new RuntimeException("Servicio de avisos MICM no disponible"))
//                 .when(header("CamelHttpResponseCode").isEqualTo(500))
//                     .log(LoggingLevel.ERROR, logger, "Error interno en servicio de avisos MICM")
//                     .throwException(new RuntimeException("Error interno en servicio de avisos"))
//                 .when(header("CamelHttpResponseCode").isEqualTo(503))
//                     .log(LoggingLevel.ERROR, logger, "Servicio de avisos MICM temporalmente no disponible")
//                     .throwException(new RuntimeException("Servicio de avisos temporalmente no disponible"))
//                 .otherwise()
//                     .log(LoggingLevel.ERROR, logger, "Error inesperado en servicio de avisos. Código: ${header.CamelHttpResponseCode}")
//                     .throwException(new RuntimeException("Error inesperado en servicio de avisos"))
//             .end();
//     }
// }