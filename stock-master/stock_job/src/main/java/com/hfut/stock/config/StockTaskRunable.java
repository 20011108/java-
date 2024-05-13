package com.hfut.stock.config;

import com.hfut.stock.service.StockTimerTaskService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Description:
 * Author:yuyang
 * Date:2024-04-28
 * Time:20:50
 */
public abstract class StockTaskRunable implements Runnable{
    //携带的任务信息,任务拒绝时，使用
    private Map<String,Object> infos;

    public StockTaskRunable(Map<String, Object> infos) {
        this.infos = infos;
    }

    //提供get方法
    public Map<String, Object> getInfos() {
        return infos;
    }
}
