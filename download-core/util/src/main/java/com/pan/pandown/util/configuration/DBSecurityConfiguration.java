package com.pan.pandown.util.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("pandown.db-security")
public class DBSecurityConfiguration {
    private boolean pwdEncrypt;
}
