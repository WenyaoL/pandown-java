package com.pan.pandown.web.config.snowflake;

import com.pan.pandown.util.mybatisPlus.SnowflakeGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-05
 */
@Configuration
public class StaticRandomConfig {

    @Bean
    @ConditionalOnMissingBean
    public Random staticRandom() {
        return new Random();
    }
}
