package com.hfut.stock.config;

import com.hfut.stock.pojo.vo.StockInfoConfig;
import com.hfut.stock.pojo.vo.TaskThreadPoolInfo;
import com.hfut.stock.utils.IdWorker;
import com.hfut.stock.utils.ParserStockInfoUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author by itheima
 * @Date 2021/12/30
 * @Description 定义公共配置类
 */
@Configuration
@EnableConfigurationProperties({StockInfoConfig.class, TaskThreadPoolInfo.class})
public class CommonConfig {

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1L,5L);
    }
    /**
     * 配置解析工具bean
     * @param idWorker
     * @return
     */
    @Bean
    public ParserStockInfoUtil parserStockInfoUtil(IdWorker idWorker){
        return new ParserStockInfoUtil(idWorker);
    }
}