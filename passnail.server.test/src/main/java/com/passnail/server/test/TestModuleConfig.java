package com.passnail.server.test;

import com.passnail.server.core.app.config.AppConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by: Pszemko at czwartek, 11.03.2021 17:53
 * Project: passnail-server
 */
@Configuration
@Import(AppConfig.class)
public class TestModuleConfig {
}
