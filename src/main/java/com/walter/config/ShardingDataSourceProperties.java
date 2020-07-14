package com.walter.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties("shardingsphere.datasource")
public class ShardingDataSourceProperties {
    private String name;
    private String driverClassName;
    private List<String> hosts;
    private String urlPattern;
    private String username;
    private String password;
}
