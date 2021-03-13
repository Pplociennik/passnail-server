package com.passnail.server.core.app.service;

import com.passnail.server.core.app.dto.LoginDto;
import com.passnail.server.core.app.dto.UserDto;

/**
 * Created by: Pszemko at sobota, 13.03.2021 18:57
 * Project: passnail-server
 */
public interface AuthServiceIf {

    UserDto authorizeOnlineUser(LoginDto aDto);
}
