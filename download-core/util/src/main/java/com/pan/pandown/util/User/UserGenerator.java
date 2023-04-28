package com.pan.pandown.util.User;

import com.pan.pandown.util.mybatisPlus.SnowflakeGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserGenerator {
    public static void main(String[] args) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        long id = new SnowflakeGenerator(1, 1).nextId();
        System.out.println("id:"+id);
        System.out.println("密码:"+bCryptPasswordEncoder.encode("123456"));
    }
}
