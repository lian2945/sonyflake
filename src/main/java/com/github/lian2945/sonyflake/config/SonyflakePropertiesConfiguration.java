package com.github.lian2945.sonyflake.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackages = "com.github.lian2945.sonyflake.properties")
public class SonyflakePropertiesConfiguration {
}
