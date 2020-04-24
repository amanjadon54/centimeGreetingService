package com.centime.greeting.controller;

import com.centime.greeting.model.GreetingRequest;
import com.centime.greeting.service.GreetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.centime.util.constants.StringConstants.ACCESS_TOKEN;

@RestController
public class GreetingController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GreetingService greetingService;

    @GetMapping("/health")
    public Object health() {
        logger.info("health check recieved");
        return "up";
    }

    @PostMapping("/greeting")
    public Object greeting(@RequestHeader Map<String, String> headers, String logId,
                           @Valid @RequestBody GreetingRequest greetingRequest) {
        return greetingService.greet(greetingRequest, headers.get(ACCESS_TOKEN), logId);
    }
}
