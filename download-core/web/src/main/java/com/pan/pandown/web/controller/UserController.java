package com.pan.pandown.web.controller;
import com.pan.pandown.dao.entity.UserInfo;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wenyao
 * @since 2023-04-17
 */
@RestController
@EnableSwagger2
@RequestMapping("/user")
public class UserController {
    // POST /user/login
    /*@PostMapping("/login")
    @CrossOrigin
    public Map<String, Object> login(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        System.out.println(username);
        if ("admin".equals(username) && "111111".equals(password)) {
            Map<String, Object> data = new HashMap<>();
            data.put("token", "admin-token");
            Map<String, Object> result = new HashMap<>();
            result.put("code", 20000);
            result.put("data", data);
            return result;
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 50001);
            result.put("message", "用户名或密码错误");
            return result;
        }
    }*/
}
