package com.hfut.stock;

import com.google.common.collect.Lists;
import com.hfut.stock.mapper.StockBusinessMapper;
import com.hfut.stock.service.StockTimerTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Author:yuyang
 * Date:2024-04-27
 * Time:14:41
 */
@SpringBootTest
public class TestStockTimerService {
    @Autowired
    private StockTimerTaskService stockTimerService;

    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    /**
     * 获取大盘数据
     */
    @Test
    public void test01(){
        stockTimerService.getInnerMarketInfo();
    }
    /**
     * 获取个股getAllStockCode
     */
    @Test
    public void test02(){
        List<String> allStockCode = stockBusinessMapper.getAllStockCode();
        allStockCode=allStockCode.stream().map(code->code.startsWith("6")?"sh"+code:"sz"+code).collect(Collectors.toList());
        Lists.partition(allStockCode, 15).forEach(li-> System.out.println(li.size()));
    }
    @Test
    public void test03(){
        stockTimerService.getStockRtIndex();
    }
    @Test
    public void test04(){
        stockTimerService.getStockBlockInfo();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
