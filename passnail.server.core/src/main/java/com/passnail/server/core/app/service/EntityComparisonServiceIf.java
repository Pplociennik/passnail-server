package com.passnail.server.core.app.service;


import com.passnail.server.core.app.entity.CredentialsEntity;
import com.passnail.server.core.app.entity.UserEntity;

import java.util.List;

/**
 * Created by: Pszemko at wtorek, 09.03.2021 18:44
 * Project: passnail-server
 */
public interface EntityComparisonServiceIf {

    List<CredentialsEntity> filterToCreateForServer(UserEntity aUserFromClient, UserEntity aUserFromServer);

    List<CredentialsEntity> filterToUpdateForServer(UserEntity aUserFromClient, UserEntity aUserFromServer);

    List<CredentialsEntity> filterToUpdateForClient(UserEntity aUserFromClient, UserEntity aUserFromServer);

    List<CredentialsEntity> filterToRemoveOnClient(UserEntity aUserFromClient, UserEntity aUserFromServer);

    List<CredentialsEntity> filterToCreateForClient(UserEntity aUserFromClient, UserEntity aUserFromServer);

    List<CredentialsEntity> filterToRemoveOnServer(UserEntity userFromClient, UserEntity userFromServer);
}
