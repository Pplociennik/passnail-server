package com.passnail.server.data.service;

import com.passnail.data.transfer.model.dto.UserDto;

/**
 * Created by: Pszemko at wtorek, 09.03.2021 18:51
 * Project: passnail-server
 */
public interface SynchronizationServiceIf {

    UserDto synchronizeServer(UserDto aUserFromClient);

    String createOnlineUserAndReturnOnlineId(UserDto aDto);
}
