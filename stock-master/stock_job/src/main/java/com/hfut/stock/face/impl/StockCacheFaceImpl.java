package com.hfut.stock.face.impl;

import com.hfut.stock.face.StockCacheFace;
import com.hfut.stock.mapper.StockBusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Author:yuyang
 * Date:2024-05-05
 * Time:15:37
 */
@Component("stockCacheFace")
public class StockCacheFaceImpl implements StockCacheFace {
    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    @Override
    @Cacheable(cacheNames = "stock",key = "'getAllStockCodeWithPredix'")
    public List<String> getAllStockCodeWithPredix() {
        //1.获取所有A股股票的编码
        List<String> allCodes = stockBusinessMapper.getAllStockCode();
        //2.添加股票前缀 sh sz
        List<String> prefixCodes = allCodes.stream().map(code -> {
            code = code.startsWith("6") ? "sh" + code : "sz" + code;
            return code;
        }).collect(Collectors.toList());
        return prefixCodes;
    }
}
