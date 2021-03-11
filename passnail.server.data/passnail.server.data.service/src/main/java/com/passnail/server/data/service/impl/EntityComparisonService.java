package com.passnail.server.data.service.impl;

import com.passnail.data.model.entity.CredentialsEntity;
import com.passnail.data.model.entity.UserEntity;
import com.passnail.data.model.status.CredentialsStatus;
import com.passnail.server.data.service.EntityComparisonServiceIf;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

import static com.passnail.data.model.status.CredentialsStatus.MAINTAINED;
import static java.util.stream.Collectors.toList;

/**
 * Created by: Pszemko at poniedziałek, 08.03.2021 19:41
 * Project: passnail-server
 */
@Service
public class EntityComparisonService implements EntityComparisonServiceIf {


    @Override
    public List<CredentialsEntity> filterToCreateForServer(UserEntity aUserFromClient, UserEntity aUserFromServer) {
        return aUserFromClient.getSavedCredentials().stream()
                .filter(fromClient -> !aUserFromServer.getSavedCredentials().contains(fromClient))
                .collect(toList());
    }

    @Override
    public List<CredentialsEntity> filterToCreateForClient(UserEntity aUserFromClient, UserEntity aUserFromServer) {
        return aUserFromServer.getSavedCredentials().stream()
                .filter(fromServer -> !aUserFromClient.getSavedCredentials().contains(fromServer))
                .filter(fromServer -> fromServer.getStatus().equals(MAINTAINED))
                .collect(toList());
    }

    @Override
    public List<CredentialsEntity> filterToUpdateForServer(UserEntity aUserFromClient, UserEntity aUserFromServer) {
        List<CredentialsEntity> resultList = new LinkedList<>();

        for (CredentialsEntity fromClient : aUserFromClient.getSavedCredentials()) {
            for (CredentialsEntity fromServer : aUserFromServer.getSavedCredentials()) {
                if (fromClient.getCredsID().equals(fromServer.getCredsID()) &&
                        fromClient.getLastModificationDate().after(fromServer.getLastModificationDate())) {
                    resultList.add(fromClient);
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
                    resultList.add(fromClient);
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
                .filter(fromServer -> fromServer.getStatus().equals(CredentialsStatus.REMOVED))
                .collect(toList());
    }


}
