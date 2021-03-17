package com.passnail.server.core.app.service.map;



import com.passnail.server.core.app.dto.CredentialsDto;
import com.passnail.server.core.app.dto.UserDto;
import com.passnail.server.core.app.entity.CredentialsEntity;
import com.passnail.server.core.app.entity.UserEntity;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by: Pszemko at czwartek, 11.03.2021 00:02
 * Project: passnail-server
 */
public class EntityToDtoDataMapper {

    public static List<CredentialsDto> mapManyCredentials(List<CredentialsEntity> aEntities) {
        return aEntities.stream()
                .map(aEntity -> mapSingleCredentials(aEntity))
                .collect(toList());
    }

    public static CredentialsDto mapSingleCredentials(CredentialsEntity aEntity) {
        return CredentialsDto.builder()
                .uniqueIdentifier(aEntity.getCredsID())
                .password(aEntity.getPassword())
                .login(aEntity.getLogin())
                .lastModificationDate(aEntity.getLastModificationDate())
                .description(aEntity.getDescription())
                .credentialsShortName(aEntity.getCredentialsShortName())
                .url(aEntity.getUrl())
                .creationDate(aEntity.getCreationDate())
                .status(aEntity.getStatus())
                .build();
    }

    public static UserDto mapSingleUser(UserEntity aUser) {
        return UserDto.builder()
                .onlineId(aUser.getOnlineID())
                .login(aUser.getLogin())
                .password(aUser.getPassword())
                .local(aUser.getLocal())
                .emailAddress(aUser.getEmailAddress())
                .creationDate(aUser.getCreationDate())
                .savedCredentials(mapManyCredentials(aUser.getSavedCredentials()))
                .build();
    }
}
