package com.banreservas.integration.util;

public class Constants {
	// Headers HTTP
    public static final String HEADER_CODE_RESPONSE = "CODE_RESPONSE";
    public static final String HEADER_MESSAGE_RESPONSE = "MESSAGE_RESPONSE";
    public static final String HEADER_MSG_TYPE = "msgType";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_HTTP_RESPONSE_CODE = "CamelHttpResponseCode";
    
    // HTTP Status Codes
    public static final String HTTP_STATUS_OK = "200";
    public static final String CODE_INTERNAL_ERROR = "500";
    public static final String CODE_SERVICE_ERROR = "502";
    
    // Messages
    public static final String MESSAGE_INTERNAL_ERROR = "Internal server error";
    public static final String MESSAGE_SERVICE_ERROR = "Error en servicios externos MICM";
    public static final String MESSAGE_BACKEND_ERROR = "Error al consultar el servicio de búsqueda de avisos MICM";
   
    private Constants() {
    }
}
