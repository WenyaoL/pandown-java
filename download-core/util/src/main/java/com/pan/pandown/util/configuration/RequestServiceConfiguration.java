package com.pan.pandown.util.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("pandown.download-api")
public class RequestServiceConfiguration {


    private String filelistUrl;

    private String signtimeUrl;

    private String dlinkUrl;

    private String accountstateUrl;

}
