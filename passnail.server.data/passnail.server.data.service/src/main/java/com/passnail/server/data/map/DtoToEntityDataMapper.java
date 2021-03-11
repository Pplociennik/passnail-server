package com.passnail.server.data.map;

import com.passnail.data.model.entity.CredentialsEntity;
import com.passnail.data.model.entity.UserEntity;
import com.passnail.data.transfer.model.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: Pszemko at poniedziałek, 08.03.2021 19:45
 * Project: passnail-server
 */
public class DtoToEntityDataMapper {

    public static List<UserEntity> map(List<UserDto> aDts) {

        return aDts.stream()
                .map(user -> UserEntity.builder()
                        .creationDate(user.getCreationDate())
                        .emailAddress(user.getEmailAddress())
                        .local(user.getLocal())
                        .login(user.getLogin())
                        .password(user.getPassword())
                        .savedCredentials(
                                user.getSavedCredentials().stream()
                                        .map(c -> CredentialsEntity.builder()
                                                .creationDate(c.getCreationDate())
                                                .url(c.getUrl())
                                                .credentialsShortName(c.getCredentialsShortName())
                                                .description(c.getDescription())
                                                .lastModificationDate(c.getLastModificationDate())
                                                .login(c.getLogin())
                                                .password(c.getPassword())
                                                .credsID(c.getUniqueIdentifier())
                                                .build())
                                        .collect(Collectors.toList())
                        )
                        .onlineID(user.getOnlineId())
                        .build())
                .collect(Collectors.toList());
    }
}
