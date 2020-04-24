package com.centime.greeting.external.manager;

import com.centime.greeting.external.RestApiManager;
import com.centime.util.exception.CustomRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.centime.greeting.util.RestHeadersGeneration.getRequestHeaders;

@Component
public class HelloManager extends RestApiManager {
    private static final Logger logger = LoggerFactory.getLogger(HelloManager.class);
    private final static String HELLO_URL = "/hello";

    @Value("${external.hello.base.url}")
    private String baseHelloeUrl;

    @Value("${external.hello.base.timeout:3000}")
    private int helloTimeout;

    public String getHello(String accessToken, String logId) throws CustomRuntimeException {
        logger.info("performing Hello service:hello with logId {}", logId);
        String concatenteResponse = super.get(baseHelloeUrl, HELLO_URL, null, getRequestHeaders(accessToken, logId),
                String.class, helloTimeout);
        if (concatenteResponse == null) {
            logger.error("No response received in Hello service:hello with logId {}", logId);
            throw new CustomRuntimeException("No response received in Hello service:hello.", 500, logId);
        }
        return concatenteResponse;
    }
}
