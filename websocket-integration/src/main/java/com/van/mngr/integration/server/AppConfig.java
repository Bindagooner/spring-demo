package com.van.mngr.integration.server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource(value = "classpath:application.yml", ignoreResourceNotFound = true)
public class AppConfig {
    // nothing here for now. this makes application run as war file
}
