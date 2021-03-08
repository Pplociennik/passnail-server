package com.passnail.server.core.app.config;

import com.passnail.data.DataServiceModuleConfig;
import com.passnail.data.model.DataModelModuleConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DataModelModuleConfig.class,
        DataServiceModuleConfig.class})
public class AppConfig {

}
