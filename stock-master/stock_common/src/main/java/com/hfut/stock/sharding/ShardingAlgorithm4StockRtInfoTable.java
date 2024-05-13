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
 * Description:
 * Author:yuyang
 * Date:2024-05-03
 * Time:16:16
 */
public class ShardingAlgorithm4StockRtInfoTable implements PreciseShardingAlgorithm<Date>, RangeShardingAlgorithm<Date> {
    @Override
    public String doSharding(Collection<String> tbNames, PreciseShardingValue<Date> preciseShardingValue) {
        String logicTableName = preciseShardingValue.getLogicTableName();
        String columnName = preciseShardingValue.getColumnName();
        Date curTime = preciseShardingValue.getValue();
        int yearMonth = Integer.parseInt(new DateTime(curTime).toString("yyyyMM"));
        Optional<String> res = tbNames.stream().filter(tbName -> tbName.endsWith(String.valueOf(yearMonth))).findFirst();
        if(res.isPresent()){
            return res.get();
        }
        return null;
    }

    @Override
    public Collection<String> doSharding(Collection<String> tbNames, RangeShardingValue<Date> rangeShardingValue) {
        String logicTableName = rangeShardingValue.getLogicTableName();
        String columnName = rangeShardingValue.getColumnName();
        Range<Date> timeRange = rangeShardingValue.getValueRange();
        if (timeRange.hasLowerBound()){
            Date date = timeRange.lowerEndpoint();
            int yearMonth = Integer.parseInt(new DateTime(date).toString("yyyyMM"));
            tbNames=tbNames.stream().filter(tbName->Integer.parseInt(tbName.substring(tbName.lastIndexOf("_")+1))>=yearMonth).collect(Collectors.toList());
        }
        if (timeRange.hasUpperBound()){
            Date date = timeRange.upperEndpoint();
            int yearMonth = Integer.parseInt(new DateTime(date).toString("yyyyMM"));
            tbNames=tbNames.stream().filter(tbName->Integer.parseInt(tbName.substring(tbName.lastIndexOf("_")+1))<=yearMonth).collect(Collectors.toList());
        }
        return tbNames;
    }
}
