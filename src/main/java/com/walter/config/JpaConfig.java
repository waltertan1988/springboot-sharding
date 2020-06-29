package com.walter.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EntityScan("com.walter.dao.entity")
@EnableJpaRepositories("com.walter.dao.repository")
//@PropertySource("classpath:application-sharding.yml")
//@ConfigurationProperties("shardingsphere")
public class JpaConfig implements InitializingBean {
//    @Value("${datasource.driver-class-name}")
//    private String driverClassName;
//    @Value("${datasource.url}")
//    private String jdbcUrl;
//    @Value("${datasource.username}")
//    private String dbUsername;
//    @Value("${datasource.password}")
//    private String dbPassword;
//    @Value("${datasource.hosts}")
//    private List<String> dbHostList;
//
//    private DataSource createDataSource(String dsName, int dsNum){
//        HikariConfig jdbcConfig = new HikariConfig();
//        String poolName = String.format("HikariPool-%s-%s", dsName, dsNum);
//        jdbcConfig.setPoolName(poolName);
//        jdbcConfig.setDriverClassName(driverClassName);
//        jdbcConfig.setJdbcUrl(jdbcUrl);
//        jdbcConfig.setUsername(dbUsername);
//        jdbcConfig.setPassword(dbPassword);
//        return new HikariDataSource(jdbcConfig);
//    }
//
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println();
    }
}
