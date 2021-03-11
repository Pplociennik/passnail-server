package com.passnail.server.core.app.config;

import com.passnail.data.model.DataModelModuleConfig;
import com.passnail.server.data.DataServiceModuleConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DataModelModuleConfig.class,
        DataServiceModuleConfig.class})
public class AppConfig {

}
