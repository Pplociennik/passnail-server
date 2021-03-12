package com.passnail.server.core.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by: Pszemko at piątek, 12.03.2021 00:37
 * Project: passnail-server
 */
@RestController
@RequestMapping(path = "/api/logging")
public class LoggingController {

    @RequestMapping(method = RequestMethod.POST, value = "/log")
    public void log() {
        System.out.println("Logging method called...");
    }
}
