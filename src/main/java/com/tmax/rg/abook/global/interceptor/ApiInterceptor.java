package com.tmax.rg.abook.global.interceptor;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Slf4j
public class ApiInterceptor implements HandlerInterceptor {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex)
            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        final ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;

        Map<String, String> queryParamMap = new HashMap<>();
        String queryString = "";

        if (cachingRequest.getQueryString() != null) {
            queryString += URLDecoder.decode(cachingRequest.getQueryString(), "UTF-8");
            String[] queryParams = queryString.split("&");
            for (String param : queryParams) {
                String name = param.split("=")[0];
                String value = "";
                if (param.split("=").length > 1) {
                    value = param.split("=")[1];
                }
                queryParamMap.put(name, value);
            }
        }

        JsonNode requestBody = null;
        JsonNode responseBody = null;
        String logString = String.format("\nFROM:%s, [%s] %s%s",
                cachingRequest.getRemoteAddr(), cachingRequest.getMethod(), cachingRequest.getRequestURL(),
                queryString.equals("") ? "" : "?" + queryString);


        if (cachingRequest.getContentType() != null && cachingRequest.getContentType().contains("application/json")) {
            cachingRequest.getContentAsByteArray();
            if (cachingRequest.getContentAsByteArray().length != 0) {
                requestBody = objectMapper.readTree(cachingRequest.getContentAsByteArray());
                logString += String.format("\nREQUEST: %s", requestBody);
            }
        }

        if (cachingResponse.getContentType() != null && cachingResponse.getContentType().contains("application/json")) {
            cachingResponse.getContentAsByteArray();
            if (cachingResponse.getContentAsByteArray().length != 0) {
                responseBody = objectMapper.readTree(cachingResponse.getContentAsByteArray());
                if (response.getStatus() == 200) {
                    logString += "\nOK";
                    logString += String.format("\nRESPONSE: %s", responseBody);
                } else {
                    logString += String.format("\nERROR RESPONSE: %s", responseBody);
                }
            }
        }

        logString += "\n";

        log.info(logString);

    }
}