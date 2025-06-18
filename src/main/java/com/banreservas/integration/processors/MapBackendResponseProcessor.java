package com.banreservas.integration.processors;

import com.banreservas.integration.model.outbound.BackendResponse;
import com.banreservas.integration.model.outbound.backend.ClientDto;
import com.banreservas.integration.model.outbound.backend.AddressDto;
import com.banreservas.integration.model.outbound.backend.ContactMethodsDto;
import com.banreservas.integration.model.outbound.backend.EmailDto;
import com.banreservas.integration.util.XMLGregorianCalendarUtil;

import org.tempuri.ClienteBanreservasResponse;
import org.tempuri.ClienteBanreservasResponse2;
import org.tempuri.Identificacion;
import org.tempuri.TiposDocumento;
import org.tempuri.ArrayOfIdentificacion;
import org.tempuri.ArrayOfDireccionesDatos;
import org.tempuri.DireccionesDatos;
import org.tempuri.ArrayOfTelefonos;
import org.tempuri.Telefonos;
import org.tempuri.ArrayOfCorreos;
import org.tempuri.Correos;
import org.tempuri.ArrayOfSistemaRelacionado;
import org.tempuri.SistemaRelacionado;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@ApplicationScoped
@Named("mapBackendResponseProcessor")
@RegisterForReflection
public class MapBackendResponseProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(MapBackendResponseProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        ClienteBanreservasResponse response = new ClienteBanreservasResponse();
        ClienteBanreservasResponse2 result = new ClienteBanreservasResponse2();

        mapRequestDataToResponse(exchange, result);

        try {
            BackendResponse backendResponse = exchange.getIn().getBody(BackendResponse.class);
            
            if (backendResponse != null && backendResponse.header() != null) {
                
                if (backendResponse.header().responseCode() == 200) {
                    setSuccessResponse(result, backendResponse);
                    mapClientData(result, backendResponse);
                } else {
                    setErrorResponse(result, backendResponse.header().responseMessage());
                }
            } else {
                setErrorResponse(result, "Error: Respuesta inválida del servicio backend Cliente Banreservas");
                logger.error("Respuesta del backend es nula o sin header");
            }
        } catch (Exception e) {
            logger.error("Error procesando respuesta del backend: {}", e.getMessage(), e);
            setErrorResponse(result, "Error interno del servidor al procesar datos del cliente: " + e.getMessage());
        }

        response.setClienteBanreservasResult(result);
        exchange.getIn().setBody(response);

        String sessionId = exchange.getIn().getHeader("sessionId", String.class);
        if (sessionId != null) {
            exchange.getIn().setHeader("sessionId", sessionId);
        }
    }

    private void mapRequestDataToResponse(Exchange exchange, ClienteBanreservasResponse2 result) {
        result.setCanal(getExchangeProperty(exchange, "canalRq"));
        result.setUsuario(getExchangeProperty(exchange, "usuarioRq"));
        result.setTerminal(getExchangeProperty(exchange, "terminalRq"));
        result.setFechaHora(getExchangeProperty(exchange, "fechaHoraRq"));
        result.setVersion(getExchangeProperty(exchange, "versionRq"));
        result.setTRNID(getExchangeProperty(exchange, "idOperacionRq"));
    }

    private void setSuccessResponse(ClienteBanreservasResponse2 result, BackendResponse backendResponse) {
        result.setResultado((short) 0);
        result.setMensaje("Consulta exitosa");
    }

    private void setErrorResponse(ClienteBanreservasResponse2 result, String errorMessage) {
        result.setResultado((short) 1);
        result.setMensaje(errorMessage != null ? errorMessage : "Error en la consulta del cliente Banreservas");
        
        logger.warn("Error en respuesta del backend: {}", result.getMensaje());
    }

    private void mapClientData(ClienteBanreservasResponse2 result, BackendResponse backendResponse) {
        if (backendResponse.body() != null && backendResponse.body().client() != null) {
            ClientDto client = backendResponse.body().client();
            
            mapBasicClientInfo(result, client);
            mapIdentifications(result, client);
            mapAddresses(result, client);
            mapContactMethods(result, client);
            mapEmails(result, client);
            mapRelatedSystems(result, client);
        }
    }

    private void mapBasicClientInfo(ClienteBanreservasResponse2 result, ClientDto client) {
        result.setNombres(client.fullName());
        result.setApellidos(client.surnames());
        result.setApodo(client.nickname());
        result.setPrimerNombre(client.firstName());
        result.setSegundoNombre(client.middleName());
        result.setPrimerApellido(client.lastName());
        result.setSegundoApellido(client.maidenName());
        result.setSexo(client.gender());
        result.setFechaNacimiento(XMLGregorianCalendarUtil.parseToXMLGregorianCalendar(client.birthDate()));
        result.setEstadoCivil(client.maritalStatus());
        result.setTipoCliente(client.clientType());
        result.setTipoPersona(client.personType());
        result.setEsClienteTop(client.isTopClient());
        result.setEsEmpleadoGrupo(client.isLinkedToGroupEmployee());
        result.setEstado(client.status());
        
        if (client.employment() != null) {
            result.setCodigoActividadEconomica(client.employment().economicActivityCode());
            result.setDescripcionActividadEconomica(client.employment().economicActivity());
            result.setIngresosAnuales(parseToDecimal(client.employment().annualIncome()));
            result.setMonedaIngresos(client.employment().incomeCurrency());
        }
        
        result.setLugarNacimiento(client.birthPlace());
        result.setPaginaWeb(client.website());
        result.setCodigoPaisOrigen(client.originCountryCode());
        result.setPaisOrigen(client.originCountry());
        result.setPaisResidencia(client.countryOfResidence());
        result.setExtranjero(client.isForeigner());
        result.setNivelgeografico(client.geographicLevel());
        result.setFallecido(client.isDeceased());
        result.setFechaFallecimiento(XMLGregorianCalendarUtil.parseToXMLGregorianCalendar(client.deathDate()));
        result.setTipoResidencia(client.residenceType());
        result.setPuntoAtencionEmisora(client.issuingAttentionPoint());
        result.setSupervisor(client.supervisor());
        result.setEjecutivoDeCuenta(client.accountExecutive());
        result.setEstaEnListaNegra(client.isOnBlacklist());
        result.setRepresentanteLegal(client.legalRepresentative());
        result.setSegmentoCliente(client.segment());
        result.setCentroDeCosto(client.costCenter());
        
        if (client.employment() != null) {
            result.setLimiteEndeudamiento(parseToDecimal(client.employment().debtLimit()));
        }
    }

    private void mapIdentifications(ClienteBanreservasResponse2 result, ClientDto client) {
        ArrayOfIdentificacion identificaciones = new ArrayOfIdentificacion();
        
        Identificacion identificacion = new Identificacion();
        identificacion.setTipoIdentificacion(TiposDocumento.CEDULA);
        
        identificaciones.getIdentificacion().add(identificacion);
        result.setIdentificaciones(identificaciones);
    }

    private void mapAddresses(ClienteBanreservasResponse2 result, ClientDto client) {
        if (client.addresses() != null && !client.addresses().isEmpty()) {
            ArrayOfDireccionesDatos direcciones = new ArrayOfDireccionesDatos();
            
            for (AddressDto address : client.addresses()) {
                DireccionesDatos direccion = new DireccionesDatos();
                direccion.setApartamento(address.apartment());
                direccion.setCalle(address.street());
                direccion.setCasa(address.house());
                direccion.setCiudad(address.city());
                direccion.setCodigoPais(address.countryCode());
                direccion.setCodigoPostal(address.postalCode());
                direccion.setEdificio(address.building());
                direccion.setEsPrincipal(address.isPrimary());
                direccion.setIdLocalidad(address.localityId());
                direccion.setPais(address.country());
                direccion.setProvincia(address.province());
                direccion.setRecibeEstados(address.receivesStatements());
                direccion.setSector(address.sector());
                direccion.setTipoDireccion(address.type());
                direccion.setTipoLocal(address.localType());
                
                direcciones.getDireccion().add(direccion);
            }
            
            result.setDirecciones(direcciones);
        }
    }

    private void mapContactMethods(ClienteBanreservasResponse2 result, ClientDto client) {
        if (client.contactMethods() != null && !client.contactMethods().isEmpty()) {
            ArrayOfTelefonos telefonos = new ArrayOfTelefonos();
            
            for (ContactMethodsDto contact : client.contactMethods()) {
                Telefonos telefono = new Telefonos();
                telefono.setCompania(contact.phoneCompany());
                telefono.setEsPrincipal(contact.isPrimary());
                telefono.setExtension(String.valueOf(contact.extension()));
                telefono.setNumero(contact.number());
                telefono.setRecibeSms(contact.receivesSMS());
                telefono.setTipo(contact.type());
                
                telefonos.getTelefono().add(telefono);
            }
            
            result.setTelefonos(telefonos);
        }
    }

    private void mapEmails(ClienteBanreservasResponse2 result, ClientDto client) {
        if (client.emails() != null && !client.emails().isEmpty()) {
            ArrayOfCorreos correos = new ArrayOfCorreos();
            
            for (EmailDto email : client.emails()) {
                Correos correo = new Correos();
                correo.setDireccion(email.email());
                correo.setEsPrincipal(email.isPrimary());
                correo.setRecibeEstados(email.receivesStatements());
                
                correos.getCorreo().add(correo);
            }
            
            result.setCorreos(correos);
        }
    }

    private void mapRelatedSystems(ClienteBanreservasResponse2 result, ClientDto client) {
        if (client.relatedSystems() != null && !client.relatedSystems().isEmpty()) {
            ArrayOfSistemaRelacionado sistemas = new ArrayOfSistemaRelacionado();
            result.setSistemasRelacionados(sistemas);
        }
    }

    private java.util.Date parseToDateTime(String dateString) {
        if (dateString == null || dateString.trim().isEmpty() || "0001-01-01".equals(dateString)) {
            return new java.util.Date(0);
        }
        
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return java.util.Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException e) {
            try {
                LocalDateTime localDateTime = LocalDateTime.parse(dateString + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return java.util.Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());
            } catch (DateTimeParseException ex) {
                logger.error("Error parseando fecha '{}': {}", dateString, ex.getMessage());
                return new java.util.Date(0);
            }
        }
    }

    private BigDecimal parseToDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            logger.error("Error parseando número '{}': {}", value, e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    private String getExchangeProperty(Exchange exchange, String propertyName) {
        Object property = exchange.getProperty(propertyName);
        return property != null ? String.valueOf(property) : "UNKNOWN";
    }
}