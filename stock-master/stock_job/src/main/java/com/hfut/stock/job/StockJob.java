package com.hfut.stock.job;

import com.hfut.stock.service.StockTimerTaskService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Description: 定义股票相关数据的定时任务
 * Author:yuyang
 * Date:2024-04-28
 * Time:16:24
 */
@Component
@Slf4j
public class StockJob {
    @Autowired
    private StockTimerTaskService stockTimerTaskService;
    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("myJobHandler")
    public void demoJobHandler() throws Exception {
        log.info("当前时间：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
    }
    /**
     * 采集国内大盘
     */
    @XxlJob("InnerMarketInfo")
    public void getInnerMarketInfo() throws Exception {
        stockTimerTaskService.getInnerMarketInfo();
    }
    /**
     * 采集国外大盘
     */
    @XxlJob("OuterMarketInfo")
    public void getOuterMarketInfo() throws Exception {
        stockTimerTaskService.getOuterMarketInfo();
    }
    /**
     * 采集个股
     */
    @XxlJob("StockRtIndex")
    public void getStockRtIndex() throws Exception {
        stockTimerTaskService.getStockRtIndex();
    }
    /**
     * 采集板块
     */
    @XxlJob("StockBlockInfo")
    public void getStockBlockInfo() throws Exception {
        stockTimerTaskService.getStockBlockInfo();
    }

}
