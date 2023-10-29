package com.pan.pandown.web.config.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.pan.pandown.util.configuration.JWTConfiguration;
import com.pan.pandown.service.common.JWTService;
import com.pan.pandown.util.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author yalier(wenyao)
 * @since 2023/4/28
 * @Description 
 */
@Configuration
public class JWTConfig {


    @Autowired
    private JWTConfiguration jwtConfiguration;

    @Bean
    public Algorithm algorithm() throws Exception {
        return Algorithm.RSA256(
                (RSAPublicKey) RSAUtils.base64strToPublicKey(jwtConfiguration.getRsaPublicKey(), X509EncodedKeySpec.class),
                (RSAPrivateKey) RSAUtils.base64strToPrivateKey(jwtConfiguration.getRsaPrivateKey(), PKCS8EncodedKeySpec.class)
        );
    }

    @Bean
    public JWTService JWTService(Algorithm algorithm){
        return new JWTService(algorithm);
    }

}
