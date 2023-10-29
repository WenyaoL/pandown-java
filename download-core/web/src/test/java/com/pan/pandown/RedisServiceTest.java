package com.pan.pandown;

import com.pan.pandown.dao.entity.PandownUser;
import com.pan.pandown.dao.entity.LoginUser;
import com.pan.pandown.service.common.RedisService;
import com.pan.pandown.web.PandownApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author yalier(wenyao)
 * @since 2023/4/28
 * @Description  RedisService test
 */
@SpringBootTest(classes = PandownApplication.class)
public class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("abc", "123");
        map.put("fff","752");

        redisService.set("test:123456",map);
        Object o = redisService.get("test:123456");
        System.out.println(o);


        PandownUser pandownUser = new PandownUser(111L, "asdfasdf", "basdfasdfasdf","dasfd");
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

    @Test
    public void test2() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add("A");
        arrayList.add("B");
        arrayList.add("C");
        redisService.lPush("test:list",arrayList);

        redisService.lPush("test:list",arrayList);


    }

    @Test
    public void test3() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();

        redisService.lPush("test:list2",arrayList);
        redisService.lPush("test:list2",arrayList);


    }
}
