package com.hfut.stock.sharding;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Description:定义公共的数据库分片算法类：包含精准匹配数据库和范围匹配数据库
 *  因为分库是根据日期分库的，一年一个库，所以片键的类型是Date
 * Author:yuyang
 * Date:2024-05-03
 * Time:15:42
 */
public class CommonShardingAlgorithm4Db implements PreciseShardingAlgorithm<Date>, RangeShardingAlgorithm<Date> {
    @Override
    public String doSharding(Collection<String> dsNames, PreciseShardingValue<Date> preciseShardingValue) {
        String logicTableName = preciseShardingValue.getLogicTableName();
        String columnName = preciseShardingValue.getColumnName();
        Date curTime = preciseShardingValue.getValue();
        int year = new DateTime(curTime).getYear();
        Optional<String> res = dsNames.stream().filter(dsName -> dsName.endsWith(String.valueOf(year))).findFirst();
        if(res.isPresent()){
            return res.get();
        }
        return null;
    }

    @Override
    public Collection<String> doSharding(Collection<String> dsNames, RangeShardingValue<Date> rangeShardingValue) {
        String logicTableName = rangeShardingValue.getLogicTableName();
        String columnName = rangeShardingValue.getColumnName();
        Range<Date> timeRange = rangeShardingValue.getValueRange();
        if (timeRange.hasLowerBound()){
            Date date = timeRange.lowerEndpoint();
            int year = new DateTime(date).getYear();
            dsNames=dsNames.stream().filter(dsName->Integer.parseInt(dsName.substring(dsName.lastIndexOf("-")+1))>=year).collect(Collectors.toList());
        }
        if (timeRange.hasUpperBound()){
            Date date = timeRange.upperEndpoint();
            int year = new DateTime(date).getYear();
            dsNames=dsNames.stream().filter(dsName->Integer.parseInt(dsName.substring(dsName.lastIndexOf("-")+1))<=year).collect(Collectors.toList());
        }
        return dsNames;
    }
}
