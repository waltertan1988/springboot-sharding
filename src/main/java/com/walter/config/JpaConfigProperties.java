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
@PropertySource("classpath:application-sharding.yml")
@ConfigurationProperties("shardingsphere")
public class JpaConfigProperties {
    private String datasourceName;
    private String datasourceDriverClassName;
    private List<String> datasourceHosts;
    private String datasourceUrlPattern;
    private String datasourceUsername;
    private String datasourcePassword;
}
