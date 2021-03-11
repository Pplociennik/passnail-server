package com.passnail.server.data.service;

import com.passnail.data.model.entity.CredentialsEntity;
import com.passnail.data.model.entity.UserEntity;
import com.passnail.data.transfer.model.dto.CredentialsDto;

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
