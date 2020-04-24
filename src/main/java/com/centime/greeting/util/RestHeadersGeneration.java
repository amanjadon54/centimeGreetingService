package com.centime.greeting.util;

import com.centime.util.constants.StringConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.centime.util.constants.StringConstants.*;

public class RestHeadersGeneration {
    public static HttpHeaders getRequestHeaders(String accessToken, String xRequestId) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(new MediaType(APPLICATION, JSON));
//        requestHeaders.set(ACCESS_TOKEN, accessToken);
        requestHeaders.set(StringConstants.X_REQUEST_ID, xRequestId);
        return requestHeaders;
    }
}
