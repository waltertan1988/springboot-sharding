package com.walter.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * @author walter
 * @date 2021/2/10
 *
 * 分表规则：按sharding column对分表总数进行取余
 */
@Slf4j
public class CustomTableComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm<Long> {

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<Long> complexKeysShardingValue) {
        log.info("[CustomTableComplexKeysShardingAlgorithm.doSharding] start: {}, {}", availableTargetNames, complexKeysShardingValue);

        Long shardingValue = complexKeysShardingValue.getColumnNameAndShardingValuesMap().values().stream()
                .flatMap(Collection::stream).findFirst().orElseThrow(() -> new RuntimeException("No sharding value found"));

        long tableIndex = shardingValue % availableTargetNames.size();
        String tableName = new StringBuilder(complexKeysShardingValue.getLogicTableName()).append(tableIndex).toString();

        if(Objects.nonNull(availableTargetNames) && availableTargetNames.contains(tableName)){
            return Arrays.asList(tableName);
        }

        return Arrays.asList();
    }
}
