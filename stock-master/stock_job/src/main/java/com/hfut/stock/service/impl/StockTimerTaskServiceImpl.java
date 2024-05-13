package com.hfut.stock.service.impl;


import com.google.common.collect.Lists;
import com.hfut.stock.config.StockTaskRunable;
import com.hfut.stock.face.StockCacheFace;
import com.hfut.stock.mapper.*;
import com.hfut.stock.pojo.entity.StockBlockRtInfo;
import com.hfut.stock.pojo.entity.StockMarketIndexInfo;
import com.hfut.stock.pojo.entity.StockOuterMarketIndexInfo;
import com.hfut.stock.pojo.entity.StockRtInfo;
import com.hfut.stock.pojo.vo.StockInfoConfig;
import com.hfut.stock.service.StockTimerTaskService;
import com.hfut.stock.utils.IdWorker;
import com.hfut.stock.utils.ParseType;
import com.hfut.stock.utils.ParserStockInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Description:
 * Author:yuyang
 * Date:2024-04-27
 * Time:14:12
 */
@Service
@Slf4j
public class StockTimerTaskServiceImpl implements StockTimerTaskService {
    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @Autowired
    private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;
    @Autowired
    private StockBusinessMapper stockBusinessMapper;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Autowired
    private ParserStockInfoUtil parserStockInfoUtil;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private HttpEntity httpEntity;

    @Autowired
    private  ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private StockCacheFace stockCacheFace;

    @Override
    public void getInnerMarketInfo() {
        String url = stockInfoConfig.getMarketUrl() + String.join(",", stockInfoConfig.getInner());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        if (statusCodeValue != 200) {
            log.error("采集国内大盘数据失败,状态码：{}", statusCodeValue);
        }
        String jsData = responseEntity.getBody();
        List<StockMarketIndexInfo> entities = parserStockInfoUtil.parser4StockOrMarketInfo(jsData,ParseType.INNER);
        log.info("采集的当前国内大盘数据：{}", entities);
        int count = stockMarketIndexInfoMapper.insertBatch(entities);
        if (count > 0) {
            rabbitTemplate.convertAndSend("stockExchange","inner.market",new Date());
            log.info("插入国内大盘数据成功！批量插入了：{}条数据", count);
        } else {
            log.info("插入国内大盘数据失败！");
        }
    }

    @Override
    public void getOuterMarketInfo() {
        String url = stockInfoConfig.getMarketUrl() + String.join(",", stockInfoConfig.getOuter());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        if (statusCodeValue != 200) {
            log.error("采集国外大盘数据失败,状态码：{}", statusCodeValue);
        }
        String jsData = responseEntity.getBody();
        List<StockOuterMarketIndexInfo> entities = parserStockInfoUtil.parser4StockOrMarketInfo(jsData,ParseType.OUTER);
        log.info("采集的当前国外大盘数据：{}", entities);
        int count = stockOuterMarketIndexInfoMapper.insertBatch(entities);
        if (count > 0) {
            rabbitTemplate.convertAndSend("stockExchange","outer.market",new Date());
            log.info("插入国外大盘数据成功！批量插入了：{}条数据", count);
        } else {
            log.info("插入国外大盘数据失败！");
        }
    }

    @Override
    public void getStockRtIndex() {
//        List<String> StockCodesList = stockBusinessMapper.getAllStockCode();
//        StockCodesList = StockCodesList.stream().map(code -> code.startsWith("6") ? "sh" + code : "sz" + code).collect(Collectors.toList());
        List<String> StockCodesList=stockCacheFace.getAllStockCodeWithPredix();
        /*Lists.partition(StockCodesList, 15).forEach(strings -> {
            String url = stockInfoConfig.getMarketUrl() + String.join(",", strings);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            int statusCodeValue = responseEntity.getStatusCodeValue();
            if (statusCodeValue != 200) {
                log.error("采集个股数据失败,状态码：{}", statusCodeValue);
            }
            String jsData = responseEntity.getBody();
            List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
            int count = stockRtInfoMapper.insertBatch(list);
            if (count > 0) {
                log.info("插入个股数据成功！批量插入了：{}条数据", count);
            } else {
                log.info("插入个股数据失败！");
            }
        });*/
        // 采用线程池
        /*Lists.partition(StockCodesList, 15).forEach(strings -> {
            threadPoolTaskExecutor.execute(()->{
                String url = stockInfoConfig.getMarketUrl() + String.join(",", strings);
                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
                int statusCodeValue = responseEntity.getStatusCodeValue();
                if (statusCodeValue != 200) {
                    log.error("采集个股数据失败,状态码：{}", statusCodeValue);
                }
                String jsData = responseEntity.getBody();
                List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
                int count = stockRtInfoMapper.insertBatch(list);
                if (count > 0) {
                    log.info("插入个股数据成功！批量插入了：{}条数据", count);
                } else {
                    log.info("插入个股数据失败！");
                }
            });
        });*/
        Lists.partition(StockCodesList,15).forEach(strings->{
            Map<String, Object> taskMap = new HashMap<>();
            taskMap.put("data",strings);
            threadPoolTaskExecutor.execute(new StockTaskRunable(taskMap) {
                @Override
                public void run() {
                    String url = stockInfoConfig.getMarketUrl() + String.join(",", strings);
                    ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
                    int statusCodeValue = responseEntity.getStatusCodeValue();
                    if (statusCodeValue != 200) {
                        log.error("采集个股数据失败,状态码：{}", statusCodeValue);
                    }
                    String jsData = responseEntity.getBody();
                    List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
                    int count = stockRtInfoMapper.insertBatch(list);
                    if (count > 0) {
                        log.info("插入个股数据成功！批量插入了：{}条数据", count);
                    } else {
                        log.info("插入个股数据失败！");
                    }
                }
            });
        });

    }

    @Override
    public void getStockBlockInfo() {
        String url =stockInfoConfig.getBlockUrl();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        if (statusCodeValue != 200) {
            log.error("采集板块数据失败,状态码：{}", statusCodeValue);
        }
        String jsData = responseEntity.getBody();
        List<StockBlockRtInfo> entities = parserStockInfoUtil.parse4StockBlock(jsData);
        /*log.info("采集的当前板块数据：{}", entities);
        int count = stockBlockRtInfoMapper.insertBatch(entities);
        if (count > 0) {
            log.info("插入板块数据成功！批量插入了：{}条数据", count);
        } else {
            log.info("插入板块数据失败！");
        }*/
        /*Lists.partition(entities,20).forEach(list->{
            threadPoolTaskExecutor.execute(()->{
                log.info("采集的当前板块数据：{}", list);
                int count = stockBlockRtInfoMapper.insertBatch(list);
                if (count > 0) {
                    log.info("插入板块数据成功！批量插入了：{}条数据", count);
                } else {
                    log.info("插入板块数据失败！");
                }
            });
        });*/
        Lists.partition(entities,20).forEach(list->{
            Map<String, Object> taskMap = new HashMap<>();
            taskMap.put("data",list);
            threadPoolTaskExecutor.execute(new StockTaskRunable(taskMap) {
                @Override
                public void run() {
                    log.info("采集的当前板块数据：{}", list);
                    int count = stockBlockRtInfoMapper.insertBatch(list);
                    if (count > 0) {
                        log.info("插入板块数据成功！批量插入了：{}条数据", count);
                    } else {
                        log.info("插入板块数据失败！");
                    }
                }
            });
        });
    }

    @PostConstruct
    public void initHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Referer", "https://finance.sina.com.cn/stock/");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        httpEntity = new HttpEntity<>(headers);
    }
}


