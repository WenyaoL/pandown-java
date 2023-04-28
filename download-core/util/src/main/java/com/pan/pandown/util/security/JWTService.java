package com.pan.pandown.util.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;

import java.util.Map;


@AllArgsConstructor
public class JWTService {

    //签名
    private Algorithm algorithm;

    public static final String ISSUER = "yuyudown(yalier)";

    public String createToken(Map map) {
        String token = JWT.create()
                .withIssuer(ISSUER) //发行者
                .withPayload(map)
                .sign(algorithm);
        return token;
    }

    public Map<String, Claim> parseToken(String token) throws RuntimeException {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT verify = verifier.verify(token);
        if(!verify.getIssuer().equals(ISSUER)) throw new RuntimeException("未知发行者");
        return verify.getClaims();
    }

}
