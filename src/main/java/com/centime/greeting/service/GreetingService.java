package com.centime.greeting.service;

import com.centime.greeting.external.manager.ConcatenationManager;
import com.centime.greeting.external.manager.HelloManager;
import com.centime.greeting.model.GreetingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GreetingService {

    @Autowired
    ConcatenationManager concatenationManager;

    @Autowired
    HelloManager helloManager;

    public String greet(GreetingRequest greetingRequest, String accessToken, String logId) {

        String helloResponse = helloManager.getHello(accessToken, logId);
        String concatenateResponse = concatenationManager.concatenate(greetingRequest, accessToken, logId);
        return String.format("%s %s", helloResponse, concatenateResponse);
    }

}
