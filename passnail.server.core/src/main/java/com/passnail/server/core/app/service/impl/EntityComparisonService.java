package com.passnail.server.core.app.service.impl;


import com.passnail.server.core.app.entity.CredentialsEntity;
import com.passnail.server.core.app.entity.UserEntity;
import com.passnail.server.core.app.service.EntityComparisonServiceIf;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

import static com.passnail.server.core.app.entity.status.CredentialsStatus.MAINTAINED;
import static com.passnail.server.core.app.entity.status.CredentialsStatus.REMOVED;
import static java.util.stream.Collectors.toList;

/**
 * Created by: Pszemko at poniedziałek, 08.03.2021 19:41
 * Project: passnail-server
 */
@Service
public class EntityComparisonService implements EntityComparisonServiceIf {


    @Override
    public List<CredentialsEntity> filterToCreateForServer(UserEntity aUserFromClient, UserEntity aUserFromServer) {
        var uniqueIdsFromServer = aUserFromServer.getSavedCredentials().stream()
                .map(fromServer -> fromServer.getCredsID())
                .collect(toList());

        return aUserFromClient.getSavedCredentials().stream()
                .filter(fromClient -> !uniqueIdsFromServer.contains(fromClient.getCredsID()))
                .collect(toList());
    }

    @Override
    public List<CredentialsEntity> filterToCreateForClient(UserEntity aUserFromClient, UserEntity aUserFromServer) {
        var uniqueIdsFromClient = aUserFromClient.getSavedCredentials().stream()
                .map(fromClient -> fromClient.getCredsID())
                .collect(toList());

        return aUserFromServer.getSavedCredentials().stream()
                .filter(fromServer -> !uniqueIdsFromClient.contains(fromServer.getCredsID()))
                .filter(fromServer -> fromServer.getStatus().equals(MAINTAINED))
                .collect(toList());
    }

    @Override
    public List<CredentialsEntity> filterToUpdateForServer(UserEntity aUserFromClient, UserEntity aUserFromServer) {
        List<CredentialsEntity> resultList = new LinkedList<>();

        for (CredentialsEntity fromClient : aUserFromClient.getSavedCredentials()) {
            for (CredentialsEntity fromServer : aUserFromServer.getSavedCredentials()) {
                if (fromClient.getCredsID() != null) {
                    if (fromClient.getCredsID().equals(fromServer.getCredsID()) &&
                            fromClient.getLastModificationDate().after(fromServer.getLastModificationDate())) {
                        resultList.add(fromClient);
                    }
                }
            }
        }

        return resultList;
    }

    @Override
    public List<CredentialsEntity> filterToUpdateForClient(UserEntity aUserFromClient, UserEntity aUserFromServer) {
        List<CredentialsEntity> resultList = new LinkedList<>();

        for (CredentialsEntity fromClient : aUserFromClient.getSavedCredentials()) {
            for (CredentialsEntity fromServer : aUserFromServer.getSavedCredentials()) {
                if (fromClient.getCredsID().equals(fromServer.getCredsID()) &&
                        fromClient.getLastModificationDate().before(fromServer.getLastModificationDate())) {
                    resultList.add(fromServer);
                }
            }
        }

        return resultList;
    }

    @Override
    public List<CredentialsEntity> filterToDeleteForClient(UserEntity aUserFromClient, UserEntity aUserFromServer) {
        var onlineIdsOfClientsCredentials = aUserFromClient.getSavedCredentials().stream()
                .map(fromClient -> fromClient.getCredsID())
                .collect(toList());

        var existOnServerAndOnClient = aUserFromServer.getSavedCredentials().stream()
                .filter(fromServer -> onlineIdsOfClientsCredentials.contains(fromServer.getCredsID()))
                .collect(toList());

        return existOnServerAndOnClient.stream()
                .filter(fromServer -> fromServer.getStatus().equals(REMOVED))
                .collect(toList());
    }


}
