package com.hfut.stock.mq;

import com.github.benmanes.caffeine.cache.Cache;
import com.hfut.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Description: 监听股票变化消息
 * Author:yuyang
 * Date:2024-04-27
 * Time:17:55
 */
@Slf4j
@Component
public class MqListener {
    @Autowired
    private Cache<String,Object> caffeineCache;

    @Autowired
    private StockService stockService;
    /**
     *
     * @param date
     * @throws Exception
     */
    @RabbitListener(queues = "innerMarketQueue")
    public void acceptInnerMarketInfo(Date date)throws Exception{
        //获取时间毫秒差值
        long diffTime= DateTime.now().getMillis()-new DateTime(date).getMillis();
        //超过一分钟告警
        if (diffTime>60000) {
            log.error("采集国内大盘时间点：{},同步超时：{}ms",new DateTime(date).toString("yyyy-MM-dd HH:mm:ss"),diffTime);
        }
        caffeineCache.invalidate("innerMarketInfos");
        stockService.innerIndexAll();

    }
}
