package com.pan.pandown.web.config.snowflake;

import com.pan.pandown.util.mybatisPlus.SnowflakeGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yalier
 */
@Configuration
public class SnowflakeAutoConfig {

    @Value("${snowflake.machine-id}")
    private long machineId;

    @Value("${snowflake.data-center-id}")
    private long dataCenterId;

    @Bean
    @ConditionalOnMissingBean
    public SnowflakeGenerator snowflakeGenerator() {
        return new SnowflakeGenerator(machineId, dataCenterId);
    }
}
