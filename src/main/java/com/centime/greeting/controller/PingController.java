package com.centime.greeting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/ping")
    public Object ping() {
        logger.info("ping called ");
        return "pong";
    }
}
