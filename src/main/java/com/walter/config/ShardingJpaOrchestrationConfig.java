package com.walter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.walter.dao.repository")
@ConditionalOnClass(name = "org.apache.shardingsphere.shardingjdbc.orchestration.spring.boot.OrchestrationSpringBootConfiguration")
public class ShardingJpaOrchestrationConfig {

//    @Bean
//    public DataSource dataSource() {
//        // OrchestrationShardingDataSourceFactory 可替换成 OrchestrationMasterSlaveDataSourceFactory 或 OrchestrationEncryptDataSourceFactory
//        return OrchestrationShardingDataSourceFactory.createDataSource(
//                createDataSourceMap(), createShardingRuleConfig(), new HashMap<String, Object>(), new Properties(),
//                new OrchestrationConfiguration(createCenterConfigurationMap()));
//    }
//
//    Map<String, DataSource> createDataSourceMap() {
//        Map<String, DataSource> result = new HashMap<>();
//        result.put("ds0", DataSourceUtil.createDataSource("ds0"));
//        result.put("ds1", DataSourceUtil.createDataSource("ds1"));
//        return result;
//    }
//
//    private Map<String, CenterConfiguration> createCenterConfigurationMap() {
//        Map<String, CenterConfiguration> instanceConfigurationMap = new HashMap<String, CenterConfiguration>();
//        CenterConfiguration config = createCenterConfiguration();
//        instanceConfigurationMap.put("orchestration-sharding-data-source", config);
//        return instanceConfigurationMap;
//    }
//    private CenterConfiguration createCenterConfiguration() {
//        Properties properties = new Properties();
//        properties.setProperty("overwrite", overwrite);
//        CenterConfiguration result = new CenterConfiguration("zookeeper", properties);
//        result.setServerLists("localhost:2181");
//        result.setNamespace("sharding-sphere-orchestration");
//        result.setOrchestrationType("registry_center,config_center");
//        return result;
//    }
}
