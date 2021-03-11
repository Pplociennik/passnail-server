package com.passnail.server.data.service.impl;

import com.passnail.data.access.model.dao.UserRepository;
import com.passnail.data.model.entity.CredentialsEntity;
import com.passnail.data.model.entity.UserEntity;
import com.passnail.server.data.service.UserServiceIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by: Pszemko at wtorek, 09.03.2021 19:45
 * Project: passnail-server
 */
@Service
public class UserService implements UserServiceIf {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;


    @Override
    public boolean exists(UserEntity aUser) {
        return userRepository.findAll().stream()
                .anyMatch(v -> v.equals(aUser));
    }

    @Override
    public List<UserEntity> getAllFromDb() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity getByOnlineId(String aOnlineId) {
        return userRepository.findByOnlineID(aOnlineId);
    }

    @Override
    public List<CredentialsEntity> addNewCredentialsToUser(UserEntity aUserFromServer, List<CredentialsEntity> aCredentials) {
        aUserFromServer.getSavedCredentials().addAll(aCredentials);
        userRepository.save(aUserFromServer);

        var uniqueCredentialsIds = aCredentials.stream()
                .map(fromClient -> fromClient.getCredsID())
                .collect(toList());

        return aUserFromServer.getSavedCredentials().stream()
                .filter(fromServer -> !uniqueCredentialsIds.contains(fromServer.getCredsID()))
                .collect(toList());
    }

    @Override
    public void updateUserAndHisCredentials(UserEntity aUserFromServer, List<CredentialsEntity> toUpdateOnServer) {
        //TODO user update in case of need

        for (CredentialsEntity fromClient : toUpdateOnServer) {
            for (CredentialsEntity fromServer : aUserFromServer.getSavedCredentials()) {
                if (fromClient.getCredsID().equals(fromServer.getCredsID())) {
                    fromServer.setUrl(fromClient.getUrl());
                    fromServer.setPassword(fromClient.getPassword());
                    fromServer.setLogin(fromClient.getLogin());
                    fromServer.setLastModificationDate(new Date());
                    fromServer.setDescription(fromClient.getDescription());
                    fromServer.setCredentialsShortName(fromClient.getCredentialsShortName());
                }
            }
        }
        userRepository.save(aUserFromServer);
    }

    @Override
    public void saveInDb(UserEntity aUser) {
        userRepository.save(aUser);
    }
}
