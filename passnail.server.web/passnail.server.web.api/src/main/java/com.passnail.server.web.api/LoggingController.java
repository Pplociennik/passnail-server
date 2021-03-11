package com.passnail.server.web.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by: Pszemko at piątek, 12.03.2021 00:37
 * Project: passnail-server
 */
@Controller
@RequestMapping(path = "/api/logging")
public class LoggingController {

    @RequestMapping(method = RequestMethod.GET, path = "/log")
    public void log() {
        System.out.println("Logging method called...");
    }
}
