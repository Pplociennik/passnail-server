package com.passnail.server.core.app.service;


import com.passnail.server.core.app.entity.CredentialsEntity;
import com.passnail.server.core.app.entity.UserEntity;

import java.util.List;

/**
 * Created by: Pszemko at wtorek, 09.03.2021 19:44
 * Project: passnail-server
 */
public interface UserServiceIf {

    boolean exists(UserEntity aUser);

    List<UserEntity> getAllFromDb();

    UserEntity getByOnlineId(String aOnlineId);

    List<CredentialsEntity> addNewCredentialsToUser(UserEntity aUserFromServer, List<CredentialsEntity> aCredentials);

    void updateUserAndHisCredentials(UserEntity aUserFromServer, List<CredentialsEntity> toUpdateOnServer);

    void saveInDb(UserEntity aUser);
}
