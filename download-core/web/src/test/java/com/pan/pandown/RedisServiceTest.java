package com.pan.pandown;

import com.pan.pandown.dao.entity.PandownUser;
import com.pan.pandown.dao.model.LoginUser;
import com.pan.pandown.util.redis.RedisService;
import com.pan.pandown.web.PandownApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest(classes = PandownApplication.class)
public class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void test() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("abc", "123");
        map.put("fff","752");

        redisService.set("test:123456",map);
        Object o = redisService.get("test:123456");
        System.out.println(o);


        PandownUser pandownUser = new PandownUser(111L, "asdfasdf", "basdfasdfasdf");
        redisService.set("test:123456",pandownUser);
        Object o1 = redisService.get("test:123456");
        System.out.println(o1);

        LoginUser loginUser = new LoginUser(pandownUser);
        map = new HashMap<>();
        map.put("abc", "123");
        map.put("fff",pandownUser);
        redisService.set("test:123456",loginUser);
        Object o2 = redisService.get("test:123456");
        System.out.println(o2);
    }
}
