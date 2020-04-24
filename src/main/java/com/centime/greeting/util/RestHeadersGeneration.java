package com.centime.greeting.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class RestHeadersGeneration {
    public static HttpHeaders getRequestHeaders(String accessToken) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(new MediaType("application",  "json"));
        requestHeaders.set("access-token", accessToken);
        return requestHeaders;
    }
}
