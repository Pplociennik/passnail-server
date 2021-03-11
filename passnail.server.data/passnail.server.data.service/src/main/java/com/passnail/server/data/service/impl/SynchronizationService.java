package com.passnail.server.data.service.impl;

import com.passnail.data.model.entity.CredentialsEntity;
import com.passnail.data.model.entity.UserEntity;
import com.passnail.data.transfer.model.dto.SynchronizationResultDto;
import com.passnail.data.transfer.model.dto.UserDto;
import com.passnail.server.data.map.DtoToEntityDataMapper;
import com.passnail.server.data.service.EntityComparisonServiceIf;
import com.passnail.server.data.service.SynchronizationServiceIf;
import com.passnail.server.data.service.UserServiceIf;
import com.passnail.server.data.util.SynchronizationConstants;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.passnail.server.data.map.EntityToDtoDataMapper.map;
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

        var toCreateOnServer = entityComparisonService.filterToCreateForServer(userFromClient, userFromServer);
        var toUpdateOnServer = entityComparisonService.filterToUpdateForServer(userFromClient, userFromServer);
        var toUpdateOnClient = entityComparisonService.filterToUpdateForClient(userFromClient, userFromServer);
        var toCreateOnClient = entityComparisonService.filterToCreateForClient(userFromClient, userFromServer);
        var toDeleteOnClient = entityComparisonService.filterToDeleteForClient(userFromClient, userFromServer);

        var createdOnServer = createNewForUserOnServer(userFromServer, toCreateOnServer);
        updateUserAndUsersCredentialsOnServer(userFromServer, toUpdateOnServer);

        return createSynchUserForClient(userFromServer, toCreateOnClient, toUpdateOnClient, toDeleteOnClient, createdOnServer);
    }

    private List<CredentialsEntity> createNewForUserOnServer(UserEntity aUserFromServer, List<CredentialsEntity> toCreateOnServer) {
        return userService.addNewCredentialsToUser(aUserFromServer, toCreateOnServer);
    }

    private void updateUserAndUsersCredentialsOnServer(UserEntity aUserFromServer, List<CredentialsEntity> toUpdateOnServer) {
        userService.updateUserAndHisCredentials(aUserFromServer, toUpdateOnServer);
    }

    private SynchronizationResultDto createSynchUserForClient(UserEntity aUserFromServer, List<CredentialsEntity> toCreateOnClient, List<CredentialsEntity> toUpdateOnClient, List<CredentialsEntity> toDeleteOnClient, List<CredentialsEntity> createdOnServer) {

        return SynchronizationResultDto.builder()
                .toCreateOnClient(map(toCreateOnClient))
                .toUpdateOnClient(map(toUpdateOnClient))
                .toDeleteOnClient(map(toDeleteOnClient))
                .createdOnServer(map(createdOnServer))
                .build();
    }

    @Override
    public String createOnlineUserAndReturnOnlineId(UserDto aDto) {
        List<String> onlineIdsFromServer = userService.getAllFromDb().stream()
                .map(userEntity -> userEntity.getOnlineID())
                .collect(toList());

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
