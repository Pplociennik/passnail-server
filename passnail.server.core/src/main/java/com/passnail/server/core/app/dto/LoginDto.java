package com.passnail.server.core.app.dto;

import lombok.*;

import java.io.Serializable;

/**
 * Created by: Pszemko at sobota, 13.03.2021 18:55
 * Project: passnail-server
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class LoginDto implements Serializable {

    private String loginOrEmail;

    private String password;

    private String onlineID;
}
