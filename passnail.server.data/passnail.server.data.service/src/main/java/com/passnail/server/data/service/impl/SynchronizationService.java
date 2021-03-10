package com.passnail.server.data.service.impl;

import com.passnail.data.model.entity.CredentialsEntity;
import com.passnail.data.model.entity.UserEntity;
import com.passnail.data.transfer.model.dto.UserDto;
import com.passnail.server.data.map.DtoToEntityDataMapper;
import com.passnail.server.data.service.EntityComparisonServiceIf;
import com.passnail.server.data.service.SynchronizationServiceIf;
import com.passnail.server.data.service.UserServiceIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by: Pszemko at wtorek, 09.03.2021 18:52
 * Project: passnail-server
 */
@Service
public class SynchronizationService implements SynchronizationServiceIf {

    @Autowired
    private EntityComparisonServiceIf entityComparisonService;

    @Autowired
    private UserServiceIf userService;


    @Override
    public UserDto synchronizeServer(UserDto aUserFromClient) {
        var userFromClient = DtoToEntityDataMapper.map(List.of(aUserFromClient)).get(0);
        var userFromServer = userService.getByOnlineId(aUserFromClient.getOnlineId());

        if (userFromServer == null) {
            throw new IllegalArgumentException("User does not exist!");
        }

        var toCreateOnServer = entityComparisonService.filterToCreateForServer(userFromClient, userFromServer);
        var toUpdateOnServer = entityComparisonService.filterToUpdateForServer(userFromClient, userFromServer);
        var toUpdateOnClient = entityComparisonService.filterToUpdateForClient(userFromClient, userFromServer);
        var toCreateOnClient = entityComparisonService.filterToCreateForClient(userFromClient, userFromServer);
        var toDeleteOnClient = entityComparisonService.filterToDeleteForClient(userFromClient, userFromServer);

        createNewForUserOnServer(userFromServer, toCreateOnServer);
        updateUserAndUsersCredentialsOnServer(userFromServer, toUpdateOnServer);

        return createSynchUserForClient(toCreateOnClient, toUpdateOnClient, toDeleteOnClient);
    }

    private void createNewForUserOnServer(UserEntity aUserFromServer, List<CredentialsEntity> toCreateOnServer) {
        userService.addNewCredentialsToUser(aUserFromServer, toCreateOnServer);
    }

    private void updateUserAndUsersCredentialsOnServer(UserEntity aUserFromServer, List<CredentialsEntity> toUpdateOnServer) {
        userService.updateUserAndHisCredentials(aUserFromServer, toUpdateOnServer);
    }

    private UserDto createSynchUserForClient(List<CredentialsEntity> toCreateOnClient, List<CredentialsEntity> toUpdateOnClient, List<CredentialsEntity> toDeleteOnClient) {
    }
}
