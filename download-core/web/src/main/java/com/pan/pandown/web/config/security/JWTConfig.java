package com.pan.pandown.web.config.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.pan.pandown.util.redis.RedisService;
import com.pan.pandown.util.security.JWTService;
import com.pan.pandown.util.security.RSAUtil;
import com.pan.pandown.service.login.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author yalier(wenyao)
 * @since 2023/4/28
 * @Description 
 */
@Configuration
public class JWTConfig {

    @Value("${pandown.JWT.rsaPublicKey}")
    private String rsaPublicKey;

    @Value("${pandown.JWT.rsaPrivateKey}")
    private String rsaPrivateKey;

    @Bean
    public Algorithm algorithm() throws Exception {
        return Algorithm.RSA256(
                (RSAPublicKey) RSAUtil.base64strToPublicKey(rsaPublicKey, X509EncodedKeySpec.class),
                (RSAPrivateKey) RSAUtil.base64strToPrivateKey(rsaPrivateKey, PKCS8EncodedKeySpec.class)
        );
    }

    @Bean
    public JWTService JWTService(Algorithm algorithm){
        return new JWTService(algorithm);
    }

}
