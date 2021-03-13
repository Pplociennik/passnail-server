package com.passnail.server.core.app.service.impl;

import com.passnail.server.core.app.dto.LoginDto;
import com.passnail.server.core.app.dto.UserDto;
import com.passnail.server.core.app.entity.UserEntity;
import com.passnail.server.core.app.service.AuthServiceIf;
import com.passnail.server.core.app.service.UserServiceIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.passnail.server.core.app.service.map.EntityToDtoDataMapper.mapSingleUser;

/**
 * Created by: Pszemko at sobota, 13.03.2021 18:57
 * Project: passnail-server
 */
@Service
public class AuthService implements AuthServiceIf {

    @Autowired
    private UserServiceIf userService;

    @Autowired
    private BCryptPasswordEncoder encoder;


    @Override
    public UserDto authorizeOnlineUser(LoginDto aDto) {
        UserEntity userBeingAuthorizing = userService.getByOnlineId(aDto.getOnlineID());

        validateUser(aDto, userBeingAuthorizing);

        return mapSingleUser(userBeingAuthorizing);
    }

    private void validateUser(LoginDto aDto, UserEntity userBeingAuthorizing) {

        if (userBeingAuthorizing == null) {
            throw new IllegalArgumentException("User with online ID: " + aDto.getOnlineID() + " not found!");
        }
        if (!userBeingAuthorizing.getEmailAddress().equals(aDto.getLoginOrEmail())) {
            throw new IllegalArgumentException("Incorrect email address!");
        }
        if (!encoder.matches(aDto.getPassword(), userBeingAuthorizing.getPassword())) {
            throw new IllegalArgumentException("Incorrect password!");
        }
    }
}
