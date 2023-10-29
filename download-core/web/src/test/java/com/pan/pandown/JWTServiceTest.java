package com.pan.pandown;

import com.pan.pandown.service.common.JWTService;
import com.pan.pandown.web.PandownApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;


/**
 * @author yalier(wenyao)
 * @since 2023/4/28
 * @Description JWTService test
 */
@SpringBootTest(classes = PandownApplication.class)
public class JWTServiceTest {

    @Autowired
    private JWTService jwtService;

    @Test
    public void testJwt() throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("abc", "123");
        String token = jwtService.createToken(map);
        System.out.println(jwtService.parseToken(token));
    }
}
