package com.walter.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author walter
 * @date 2021/2/10
 *
 * 分库规则：按sharding column对分库总数进行取余
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CustomDbComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm<Long> {

    private String shardPrefix;

    private final String DEFAULT_SHARD_PREFIX = "sharding";

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<Long> complexKeysShardingValue) {
        log.info("[CustomDbComplexKeysShardingAlgorithm.doSharding] start: {}, {}", availableTargetNames, complexKeysShardingValue);

        Long shardingValue = complexKeysShardingValue.getColumnNameAndShardingValuesMap().values().stream()
                .flatMap(Collection::stream).findFirst().orElseThrow(() -> new RuntimeException("No sharding value found"));

        long dbIndex = shardingValue % availableTargetNames.size();
        String dbName = new StringBuilder(shardPrefix == null ? DEFAULT_SHARD_PREFIX : shardPrefix).append(dbIndex).toString();

        if(Objects.nonNull(availableTargetNames)
                && availableTargetNames.stream().map(String::toLowerCase).anyMatch(Predicate.isEqual(dbName.toLowerCase()))){
            return Arrays.asList(dbName);
        }

        return Arrays.asList();
    }
}
