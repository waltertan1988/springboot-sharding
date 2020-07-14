package com.walter.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Configuration
@EntityScan("com.walter.dao.entity")
@EnableJpaRepositories("com.walter.dao.repository")
public class JpaConfig {
    @Autowired
    private JpaConfigProperties jpaConfigProperties;

    @Bean
    public DataSource dataSource() throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>(jpaConfigProperties.getDatasourceHosts().size());
        String dsName = jpaConfigProperties.getDatasourceName();
        // 配置真实数据源
        for (int i = 0; i < jpaConfigProperties.getDatasourceHosts().size(); i++) {
            String dsHost = jpaConfigProperties.getDatasourceHosts().get(i);
            dataSourceMap.put(dsName + i, createSubDataSource(dsHost, i));
        }
        TableRuleConfiguration orderTableRuleConfig = getTableRuleConfiguration(dsName, 2, "user_id","t_order", 2, "order_id");
        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
        // 获取数据源对象
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new Properties());
    }

    private DataSource createSubDataSource(String host, int dsNum){
        HikariConfig jdbcConfig = new HikariConfig();
        String poolName = String.format("HikariPool-%s-%s", jpaConfigProperties.getDatasourceName(), dsNum);
        jdbcConfig.setPoolName(poolName);
        jdbcConfig.setDriverClassName(jpaConfigProperties.getDatasourceDriverClassName());
        String jdbcUrl = jpaConfigProperties.getDatasourceUrlPattern()
                .replace("\\{HOST\\}", host)
                .replace("\\{DS_NUM\\}", String.valueOf(dsNum));
        jdbcConfig.setJdbcUrl(jdbcUrl);
        jdbcConfig.setUsername(jpaConfigProperties.getDatasourceUsername());
        jdbcConfig.setPassword(jpaConfigProperties.getDatasourcePassword());
        return new HikariDataSource(jdbcConfig);
    }

    private TableRuleConfiguration getTableRuleConfiguration(String dsName, int dsCount, String dsShardignCoulumn, String tableName, int tableCount, String tableShardingColumn){
        // 配置Order表规则（ds${0..1}.t_order${0..1}）
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration(tableName, String.format("%s${0..%s}.%s${0..%s}", dsName, dsCount-1, tableName, tableCount-1));
        // 配置分库策略（ds${user_id % 2}）
        String dsShardingExpression = String.format("%s${%s % %s}", dsName, dsShardignCoulumn, dsCount);
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration(dsShardignCoulumn, dsShardingExpression));
        // 配置分表策略（t_order${order_id % 2}）
        String tableShardingExpression = String.format("%s${%s % %s}", tableName, tableShardingColumn, tableCount);
        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration(tableShardingColumn, tableShardingExpression));

        return orderTableRuleConfig;
    }
}
