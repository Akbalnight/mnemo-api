package com.dias.services.mnemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@PropertySource(value="file:${config}/application.properties", ignoreResourceNotFound = true)
public class ServiceConfig {
}
