package com.passnail.server.core.app.service;


import com.passnail.server.core.app.dto.SynchronizationResultDto;
import com.passnail.server.core.app.dto.UserDto;

/**
 * Created by: Pszemko at wtorek, 09.03.2021 18:51
 * Project: passnail-server
 */
public interface SynchronizationServiceIf {

    SynchronizationResultDto synchronizeServer(UserDto aUserFromClient);

    String createOnlineUserAndReturnOnlineId(UserDto aDto);
}
