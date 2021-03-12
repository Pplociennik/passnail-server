package com.passnail.server.core.app.controller;


import com.passnail.server.core.app.dto.SynchronizationResultDto;
import com.passnail.server.core.app.dto.UserDto;
import com.passnail.server.core.app.service.SynchronizationServiceIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/synch")
public class SynchronizationController {

    @Autowired
    private SynchronizationServiceIf synchronizationService;


    @RequestMapping(method = RequestMethod.POST, value = "/user/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String createAnOnlineUser(UserDto aUserDto) {
        return synchronizationService.createOnlineUserAndReturnOnlineId(aUserDto);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/user/synchronize", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SynchronizationResultDto synchronizeUser(UserDto aUserFromClient) {
        return synchronizationService.synchronizeServer(aUserFromClient);
    }

}
