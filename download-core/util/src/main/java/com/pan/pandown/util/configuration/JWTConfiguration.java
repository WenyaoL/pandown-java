package com.pan.pandown.util.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("pandown.jwt")
public class JWTConfiguration {

    private String rsaPublicKey;

    private String rsaPrivateKey;

    private Integer expireTime;

}
