package com.passnail.server.core.app.config;

import com.passnail.data.model.DataModelModuleConfig;
import com.passnail.security.SecurityModuleConfig;
import com.passnail.server.data.DataServiceModuleConfig;
import com.passnail.server.web.api.WebApiModuleConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DataModelModuleConfig.class,
        DataServiceModuleConfig.class,
        WebApiModuleConfig.class,
        SecurityModuleConfig.class})
public class AppConfig {

}
