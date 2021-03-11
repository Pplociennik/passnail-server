package com.passnail.server.data.map;

import com.passnail.data.model.entity.CredentialsEntity;
import com.passnail.data.transfer.model.dto.CredentialsDto;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by: Pszemko at czwartek, 11.03.2021 00:02
 * Project: passnail-server
 */
public class EntityToDtoDataMapper {

    public static List<CredentialsDto> map(List<CredentialsEntity> aEntities) {
        return aEntities.stream()
                .map(aEntity -> mapToDto(aEntity))
                .collect(toList());
    }

    private static CredentialsDto mapToDto(CredentialsEntity aEntity) {
        return CredentialsDto.builder()
                .uniqueIdentifier(aEntity.getCredsID())
                .password(aEntity.getPassword())
                .login(aEntity.getLogin())
                .lastModificationDate(aEntity.getLastModificationDate())
                .description(aEntity.getDescription())
                .credentialsShortName(aEntity.getCredentialsShortName())
                .url(aEntity.getUrl())
                .creationDate(aEntity.getCreationDate())
                .build();
    }
}
