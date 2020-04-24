package com.centime.greeting.external.manager;

import com.centime.greeting.model.GreetingRequest;
import com.centime.greeting.external.RestApiManager;
import com.centime.util.exception.CustomRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.centime.greeting.util.RestHeadersGeneration.getRequestHeaders;

@Component
public class ConcatenationManager extends RestApiManager {
    private static final Logger logger = LoggerFactory.getLogger(ConcatenationManager.class);
    private final static String CONCATENATE_URL = "/concatenate";

    @Value("${external.concatenate.base.url:localhost:10003}")
    private String baseConcatenateUrl;

    @Value("${external.concatenate.base.timeout:5000}")
    private int concatenteTimeout;

    public String concatenate(GreetingRequest greetingRequest, String accessToken, String logId) throws CustomRuntimeException {
        logger.info("performing Concatenate service:concatenate with logId {}", logId);
        String concatenteResponse = super.post(baseConcatenateUrl, CONCATENATE_URL, null, greetingRequest, getRequestHeaders(accessToken),
                String.class, 10, concatenteTimeout);
        if (concatenteResponse == null) {
            logger.error("No response received in Concatenate service:concatenate with logId {}", logId);
            throw new CustomRuntimeException("No response received in Concatenate service:concatenate.", 500, logId);
        }
        return concatenteResponse;
    }

}