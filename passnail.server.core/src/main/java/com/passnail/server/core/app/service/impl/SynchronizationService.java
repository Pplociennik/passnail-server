package com.passnail.server.core.app.service.impl;


import com.passnail.server.core.app.dto.SynchronizationResultDto;
import com.passnail.server.core.app.dto.UserDto;
import com.passnail.server.core.app.entity.CredentialsEntity;
import com.passnail.server.core.app.entity.UserEntity;
import com.passnail.server.core.app.service.EntityComparisonServiceIf;
import com.passnail.server.core.app.service.SynchronizationServiceIf;
import com.passnail.server.core.app.service.UserServiceIf;
import com.passnail.server.core.app.service.map.DtoToEntityDataMapper;
import com.passnail.server.core.app.service.util.SynchronizationConstants;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.passnail.server.core.app.service.map.EntityToDtoDataMapper.mapManyCredentials;
import static java.util.stream.Collectors.toList;

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
    public SynchronizationResultDto synchronizeServer(UserDto aUserFromClient) {
        var userFromClient = DtoToEntityDataMapper.map(List.of(aUserFromClient)).get(0);
        var userFromServer = userService.getByOnlineId(aUserFromClient.getOnlineId());

        if (userFromServer == null) {
            throw new IllegalArgumentException("User does not exist!");
        }

        var toRemoveOnServer = entityComparisonService.filterToRemoveOnServer(userFromClient, userFromServer);
        var toCreateOnServer = entityComparisonService.filterToCreateForServer(userFromClient, userFromServer);
        var toUpdateOnServer = entityComparisonService.filterToUpdateForServer(userFromClient, userFromServer);
        var toUpdateOnClient = entityComparisonService.filterToUpdateForClient(userFromClient, userFromServer);
        var toCreateOnClient = entityComparisonService.filterToCreateForClient(userFromClient, userFromServer);
        var toRemoveOnClient = entityComparisonService.filterToRemoveOnClient(userFromClient, userFromServer);

        if (!toRemoveOnServer.isEmpty()) {
            setOnServerAsRemoved(userFromServer, toRemoveOnServer);
        }

        List<CredentialsEntity> createdOnServer = new LinkedList<>();

        if (!toCreateOnServer.isEmpty()) {
            createdOnServer = createNewForUserOnServer(userFromServer, toCreateOnServer);
        }

        if (!toUpdateOnServer.isEmpty()) {
            updateUserAndUsersCredentialsOnServer(userFromServer, toUpdateOnServer);
        }

        return createSynchUserForClient(userFromServer, toCreateOnClient, toUpdateOnClient, toRemoveOnClient, createdOnServer);
    }

    private void setOnServerAsRemoved(UserEntity aUserFromServer, List<CredentialsEntity> toRemoveOnServer) {
        userService.markCredentialsAsRemoved(aUserFromServer, toRemoveOnServer);
    }

    private List<CredentialsEntity> createNewForUserOnServer(UserEntity aUserFromServer, List<CredentialsEntity> toCreateOnServer) {
        return userService.addNewCredentialsToUser(aUserFromServer, toCreateOnServer);
    }

    private void updateUserAndUsersCredentialsOnServer(UserEntity aUserFromServer, List<CredentialsEntity> toUpdateOnServer) {
        userService.updateUserAndHisCredentials(aUserFromServer, toUpdateOnServer);
    }

    private SynchronizationResultDto createSynchUserForClient(UserEntity aUserFromServer, List<CredentialsEntity> toCreateOnClient, List<CredentialsEntity> toUpdateOnClient, List<CredentialsEntity> toDeleteOnClient, List<CredentialsEntity> createdOnServer) {

        return SynchronizationResultDto.builder()
                .toCreateOnClient(mapManyCredentials(toCreateOnClient))
                .toUpdateOnClient(mapManyCredentials(toUpdateOnClient))
                .toDeleteOnClient(mapManyCredentials(toDeleteOnClient))
                .createdOnServer(mapManyCredentials(createdOnServer))
                .build();
    }

    @Override
    public String createOnlineUserAndReturnOnlineId(UserDto aDto) {
        List<String> onlineIdsFromServer = userService.getAllFromDb().stream()
                .map(userEntity -> userEntity.getOnlineID())
                .collect(toList());

        if (aDto.getOnlineId() != null) {
            throw new IllegalArgumentException("User has an online ID already generated!");
        }

        PasswordGenerator generator = new PasswordGenerator();

        CharacterData lowerCaseData = EnglishCharacterData.LowerCase;
        CharacterData upperCaseData = EnglishCharacterData.UpperCase;
        CharacterData digitsData = EnglishCharacterData.Digit;

        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseData);
        CharacterRule upperCaseRule = new CharacterRule(upperCaseData);
        CharacterRule digitsRule = new CharacterRule(digitsData);

        lowerCaseRule.setNumberOfCharacters(SynchronizationConstants.ONLINE_ID_DEFAULT_LOWER_CASE_LETTERS);
        upperCaseRule.setNumberOfCharacters(SynchronizationConstants.ONLINE_ID_DEFAULT_UPPER_CASE_LETTERS);
        digitsRule.setNumberOfCharacters(SynchronizationConstants.ONLINE_ID_DEFAULT_DIGITS);

        String newUniqueOnlineId;

        do {
            newUniqueOnlineId = generator.generatePassword(SynchronizationConstants.ONLINE_ID_DEFAULT_LENGTH, lowerCaseRule, upperCaseRule, digitsRule);
        } while (onlineIdsFromServer.contains(newUniqueOnlineId));

        createNewOnlineUser(aDto, newUniqueOnlineId);

        return newUniqueOnlineId;
    }

    private void createNewOnlineUser(UserDto aDto, String newUniqueOnlineId) {
        UserEntity newOnlineUser = UserEntity.builder()
                .onlineID(newUniqueOnlineId)
                .creationDate(aDto.getCreationDate())
                .emailAddress(aDto.getEmailAddress())
                .lastModificationDate(new Date())
                .local(false)
                .login(aDto.getLogin())
                .password(aDto.getPassword())
                .build();

        userService.saveInDb(newOnlineUser);
    }
}
