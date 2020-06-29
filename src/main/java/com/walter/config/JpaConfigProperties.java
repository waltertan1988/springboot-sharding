package com.walter.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-sharding.yml")
@ConfigurationProperties("shardingsphere")
public class JpaConfigProperties implements InitializingBean {
    @Value("${datasource.driver-class-name}")
    private String driverClassName;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println();
    }
}
