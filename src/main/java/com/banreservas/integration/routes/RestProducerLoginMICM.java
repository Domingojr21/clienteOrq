// package com.banreservas.integration.routes;

// import org.apache.camel.Exchange;
// import org.apache.camel.LoggingLevel;
// import org.apache.camel.builder.RouteBuilder;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import jakarta.enterprise.context.ApplicationScoped;

// /**
//  * Producer para el servicio de login MICM.
//  * Maneja la autenticación y obtención de tokens JWT.
//  */
// @ApplicationScoped
// public class RestProducerLoginMICM extends RouteBuilder {
    
//     private static final Logger logger = LoggerFactory.getLogger(RestProducerLoginMICM.class);
    
//     @Override
//     public void configure() throws Exception {
        
//         from("direct:login-micm-service")
//             .routeId("login-micm-service-route")
//             .log(LoggingLevel.INFO, logger, "Iniciando autenticación con servicio MICM")
            
//             .removeHeaders("CamelHttp*")
//             .removeHeader("host")
            
//             .setHeader(Exchange.HTTP_METHOD, constant("POST"))
//             .setHeader("Accept", constant("application/json"))
//             .setHeader("Content-Type", constant("application/json"))
//             .setHeader("sessionId", constant("1"))
            
//             .toD("{{login.micm.service.url}}?bridgeEndpoint=true&throwExceptionOnFailure=false&connectTimeout={{login.service.timeout}}&socketTimeout={{login.service.timeout}}")
            
//             .choice()
//                 .when(header("CamelHttpResponseCode").isEqualTo(200))
//                     .log(LoggingLevel.INFO, logger, "Autenticación MICM exitosa")
//                 .when(header("CamelHttpResponseCode").isEqualTo(401))
//                     .log(LoggingLevel.ERROR, logger, "Credenciales inválidas para servicio MICM")
//                 .when(header("CamelHttpResponseCode").isEqualTo(404))
//                     .log(LoggingLevel.ERROR, logger, "Servicio de login MICM no encontrado")
//                 .when(header("CamelHttpResponseCode").isEqualTo(500))
//                     .log(LoggingLevel.ERROR, logger, "Error interno en servicio MICM")
//                 .otherwise()
//                     .log(LoggingLevel.ERROR, logger, "Error inesperado en login MICM. Código: ${header.CamelHttpResponseCode}")
//             .end();
//     }
// }