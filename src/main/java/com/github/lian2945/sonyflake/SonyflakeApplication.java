package com.github.lian2945.sonyflake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.github.lian2945.sonyflake.properties.SonyflakeProperties;

@SpringBootApplication
@EnableConfigurationProperties(SonyflakeProperties.class)
public class SonyflakeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SonyflakeApplication.class, args);
    }

}
