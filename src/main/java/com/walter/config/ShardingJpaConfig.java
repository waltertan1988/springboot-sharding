package com.walter.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EntityScan("com.walter.dao.entity")
@EnableJpaRepositories("com.walter.dao.repository")
public class ShardingJpaConfig {
    @Autowired
    private ShardingDataSourceProperties shardingDataSourceProperties;

    @Bean
    public DataSource dataSource() {
        Map<String, DataSource> dataSourceMap = new HashMap<>(shardingDataSourceProperties.getHosts().size());
        String dsName = shardingDataSourceProperties.getName();
        // 配置真实数据源
        for (int i = 0; i < shardingDataSourceProperties.getHosts().size(); i++) {
            String dsHost = shardingDataSourceProperties.getHosts().get(i);
            dataSourceMap.put(dsName + i, createSubDataSource(dsHost, i));
        }
        TableRuleConfiguration orderTableRuleConfig = newTableRuleConfiguration(dsName, 2, "user_id",
                "t_order", 2, "order_id");
        TableRuleConfiguration orderItemTableRuleConfig = newTableRuleConfiguration(dsName, 2, "user_id",
                "t_order_item", 2, "order_id");
        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
        shardingRuleConfig.getTableRuleConfigs().add(orderItemTableRuleConfig);
        // 获取数据源对象
        try {
            return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new Properties());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource());
        //加载实体类
        bean.setPackagesToScan("com.walter.dao.entity");
        bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        bean.setJpaProperties(properties);
        return bean;
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }

    private DataSource createSubDataSource(String host, int dsNum){
        HikariConfig jdbcConfig = new HikariConfig();
        String poolName = String.format("HikariPool-%s-%s", shardingDataSourceProperties.getName(), dsNum);
        jdbcConfig.setPoolName(poolName);
        jdbcConfig.setDriverClassName(shardingDataSourceProperties.getDriverClassName());
        String jdbcUrl = shardingDataSourceProperties.getUrlPattern()
                .replace("{HOST}", host)
                .replace("{DS_NUM}", String.valueOf(dsNum));
        jdbcConfig.setJdbcUrl(jdbcUrl);
        jdbcConfig.setUsername(shardingDataSourceProperties.getUsername());
        jdbcConfig.setPassword(shardingDataSourceProperties.getPassword());
        return new HikariDataSource(jdbcConfig);
    }

    /**
     * 配置表规则
     * @param dsName
     * @param dsCount
     * @param dsShardignCoulumn
     * @param tableName
     * @param tableCount
     * @param tableShardingColumn
     * @return
     */
    private TableRuleConfiguration newTableRuleConfiguration(String dsName, int dsCount, String dsShardignCoulumn, String tableName, int tableCount, String tableShardingColumn){
        // 配置表规则（ds${0..1}.t_order${0..1}）
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration(tableName, String.format("%s%s{0..%s}.%s%s{0..%s}", dsName, "$", dsCount-1, tableName, "$", tableCount-1));
        // 配置分库策略（ds${user_id % 2}）
        String dsShardingExpression = String.format("%s%s{%s %% %s}", dsName, "$", dsShardignCoulumn, dsCount);
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration(dsShardignCoulumn, dsShardingExpression));
        // 配置分表策略（t_order${order_id % 2}）
        String tableShardingExpression = String.format("%s%s{%s %% %s}", tableName, "$", tableShardingColumn, tableCount);
        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration(tableShardingColumn, tableShardingExpression));

        return orderTableRuleConfig;
    }
}
