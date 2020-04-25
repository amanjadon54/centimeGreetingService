package com.centime.greeting.external;

import com.centime.greeting.configuration.GreetingConfiguration;
import com.centime.util.exception.CustomRuntimeException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestApiManager {

    @Autowired
    private GreetingConfiguration appConfiguration;

    private static final Gson gson = new Gson();

    private static final Logger log = LoggerFactory.getLogger(RestApiManager.class);

    public <T> T get(String baseUrl, String url, String query, HttpHeaders requestHeaders,
                     Class<T> responseClassType, int readTimeout, String logId) {
        ResponseEntity<T> responseEntity = null;
        try {
            String fullUrl = getFullUrl(baseUrl, url, query);
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestHeaders);
            RestTemplate restTemplate = appConfiguration.restTemplate();
            HttpComponentsClientHttpRequestFactory rf = (HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory();
            rf.setReadTimeout(readTimeout);
            log.info("The URL called : {} and readTimeout sent : {}", fullUrl, readTimeout);
            responseEntity = restTemplate.exchange(fullUrl, HttpMethod.GET, requestEntity, responseClassType);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            }
        } catch (Exception e) {
            log.error("Error in RestApiManager:get : {} ; Exception : {}", responseEntity, e);
            if (responseEntity != null) {
                throw new CustomRuntimeException(e.getMessage(), Integer.parseInt(responseEntity.getStatusCode().toString()), "Get service:concatenation failing" + logId, null);
            } else {
                throw new CustomRuntimeException(e.getMessage(), 500, "Get service:concatenation failing " + logId, null);
            }
        }
        return null;
    }

    public <T> T post(String baseUrl, String url, String query, JsonObject body,
                      HttpHeaders requestHeaders, Class<T> responseClassType, int connectTimeout, int readTimeout, String logId) {
        ResponseEntity<T> responseEntity = null;
        try {
            String fullUrl = getFullUrl(baseUrl, url, query);
            HttpEntity<Object> requestEntity = new HttpEntity<>(body.toString(), requestHeaders);

            RestTemplate restTemplate = appConfiguration.restTemplate();
            HttpComponentsClientHttpRequestFactory rf = (HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory();
            rf.setReadTimeout(readTimeout);
            log.info("The URL called : {} and readTimeout sent : {}", fullUrl, readTimeout);

            responseEntity =
                    restTemplate.exchange(fullUrl, HttpMethod.POST, requestEntity, responseClassType);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            }
        } catch (Exception e) {
            log.error("Error in RestApiManager:post : {} ; Exception : {}", responseEntity, e);
            if (responseEntity != null) {
                throw new CustomRuntimeException(e.getMessage(), Integer.parseInt(responseEntity.getStatusCode().toString()), "post service:concatenation failing " + logId, null);
            } else {
                throw new CustomRuntimeException(e.getMessage(), 500, "post service:concatenation failing " + logId, null);
            }
        }
        return null;
    }

    private String getFullUrl(String baseUrl, String url, String query) {
        StringBuilder fullUrl = new StringBuilder();
        fullUrl.append(baseUrl);
        if (url != null) {
            fullUrl.append(url);
        }
        if (query != null && query.startsWith("?")) {
            query = query.substring(1);
        }
        query = StringUtils.trimToNull(query);
        if (query != null) {
            fullUrl.append("?");
            fullUrl.append(query);
        }
        return fullUrl.toString();
    }

    private static String toJson(Object obj) {
        try {
            if (obj != null) {
                return gson.toJson(obj);
            }
        } catch (JsonParseException e) {
            log.error("Error in toJson(), obj: " + obj + " ; Exception: " + e.getMessage());
        }
        return null;
    }
}
