package com.centime.greeting.external.manager;

import com.centime.greeting.model.GreetingRequest;
import com.centime.greeting.external.RestApiManager;
import com.centime.util.exception.CustomRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.centime.greeting.util.RestHeadersGeneration.getRequestHeaders;

@Component
public class ConcatenationManager extends RestApiManager {
    private static final Logger logger = LoggerFactory.getLogger(ConcatenationManager.class);
    private final static String CONCATENATE_URL = "/concatenate";

    @Value("${external.concatenate.base.url}")
    private String baseConcatenateUrl;

    @Value("${external.concatenate.base.timeout}")
    private int concatenteTimeout;

    public String concatenate(GreetingRequest greetingRequest, String accessToken, String logId) {
        logger.info("performing Concatenate service:concatenate with logId {}", logId);

        ObjectMapper mapper = new ObjectMapper();
        String concatenteResponse = null;
        try {
            Object request = mapper.writeValueAsString(greetingRequest);

            JsonObject concatenateEntity = new JsonObject();
            concatenateEntity.addProperty("Name", greetingRequest.getName());
            concatenateEntity.addProperty("Sirname", greetingRequest.getSirName());

            concatenteResponse = super.post(baseConcatenateUrl, CONCATENATE_URL, null, concatenateEntity, getRequestHeaders(accessToken),
                    String.class, 10, concatenteTimeout);
            if (concatenteResponse == null) {
                logger.error("No response received in Concatenate service:concatenate with logId {}", logId);
                throw new CustomRuntimeException("No response received in Concatenate service:concatenate.", 500, logId);
            }
        } catch (JsonProcessingException e) {

        }
        return concatenteResponse;
    }

}