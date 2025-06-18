package com.banreservas.integration.mocks;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SoapWireMock implements QuarkusTestResourceLifecycleManager {

    static final Logger logger = Logger.getLogger(SoapWireMock.class);
    
    private static String responseLoginMICM;
    private static String responseBusquedaAvisos;

    private static WireMockServer server;

    @Override
    public Map<String, String> start() {

        logger.info("Starting WireMock Server");

        server = new WireMockServer(wireMockConfig()
                .dynamicPort());
        server.start();

        try {
            responseLoginMICM = new String(
                    Files.readAllBytes(Paths.get("src/test/resources/response/ResponseLoginMICM.json")));
            responseBusquedaAvisos = new String(
                    Files.readAllBytes(Paths.get("src/test/resources/response/ResponseBusquedaAvisos.json")));

        } catch (IOException e) {
            logger.error("ERROR! en el catch de request y response" + e.getMessage());
            throw new RuntimeException(e);
        }

        server.stubFor(post(urlMatching("/api/v1/login-micm.*"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseLoginMICM)
                        .withStatus(200)));

        server.stubFor(post(urlMatching("/api/v1/busqueda-general-avisos-micm.*"))
                .withHeader("test", equalTo("ok"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBusquedaAvisos)
                        .withStatus(200)));

        server.stubFor(post(urlMatching("/api/v1/busqueda-general-avisos-micm.*"))
                .withHeader("test", absent())
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\": \"Unauthorized\"}")
                        .withStatus(401)));

        server.stubFor(any(urlMatching(".*"))
                .atPriority(10) 
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("Not found in WireMock")));

        Map<String, String> config = new HashMap<>();
        config.put("backend.notices.service.url", server.baseUrl() + "/api/v1/busqueda-general-avisos-micm");
        config.put("login.micm.service.url", server.baseUrl() + "/api/v1/login-micm");
        config.put("backend.service.timeout", "5000");
        config.put("login.service.timeout", "5000");
        
        logger.info("WireMock started on: " + server.baseUrl());
        logger.info("Login URL: " + config.get("login.micm.service.url"));
        logger.info("Backend URL: " + config.get("backend.notices.service.url"));
        
        return config;
    }

    @Override
    public void stop() {
        logger.info("Stopping WireMock Server");

        if (server != null) {
            server.stop();
        }
        logger.info("WireMock Server Stopped");
    }
}