package com.passnail.server.web.api;


import com.passnail.data.transfer.model.dto.SynchronizationResultDto;
import com.passnail.data.transfer.model.dto.UserDto;
import com.passnail.server.data.service.SynchronizationServiceIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = "/api/synch")
public class SynchronizationController {

    @Autowired
    private SynchronizationServiceIf synchronizationService;


    @RequestMapping(method = RequestMethod.POST, path = "/user/create")
    public String createAnOnlineUser(UserDto aUserDto) {
        return synchronizationService.createOnlineUserAndReturnOnlineId(aUserDto);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/user/synchronize")
    public SynchronizationResultDto synchronizeUser(UserDto aUserFromClient) {
        return synchronizationService.synchronizeServer(aUserFromClient);
    }

}
