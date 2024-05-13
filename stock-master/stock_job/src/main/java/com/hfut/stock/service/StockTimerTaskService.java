package com.hfut.stock.service;

/**
 * Description: 定义采集股票数据的定时任务的服务接口
 * Author:yuyang
 * Date:2024-04-27
 * Time:14:10
 */
public interface StockTimerTaskService {
    /**
     * 获取国内大盘的实时数据信息
     */
    void getInnerMarketInfo();
    /**
     * 获取国外大盘的实时数据信息
     */
    void getOuterMarketInfo();
    /**
     * 定义获取分钟级股票数据
     */
    void getStockRtIndex();
    /**
     * 定义获取分钟级股票数据
     */
    void getStockBlockInfo();
}
