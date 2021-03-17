package com.passnail.server.core.app.controller;

import com.passnail.server.core.app.dto.LoginDto;
import com.passnail.server.core.app.dto.UserDto;
import com.passnail.server.core.app.service.AuthServiceIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by: Pszemko at sobota, 13.03.2021 18:53
 * Project: passnail-server
 */
@RestController
@RequestMapping(value = "api/auth")
public class AuthController {

    @Autowired
    private AuthServiceIf authService;

    @RequestMapping(method = RequestMethod.POST, value = "/user")
    public UserDto authorizeOnlineUser(@RequestBody LoginDto aDto) {
        return authService.authorizeOnlineUser(aDto);
    }
}
