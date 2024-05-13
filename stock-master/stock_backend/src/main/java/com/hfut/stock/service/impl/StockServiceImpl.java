package com.hfut.stock.service.impl;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hfut.stock.mapper.*;
import com.hfut.stock.pojo.domin.*;
import com.hfut.stock.pojo.vo.StockInfoConfig;
import com.hfut.stock.service.StockService;
import com.hfut.stock.utils.DateTimeUtil;
import com.hfut.stock.vo.resp.PageResult;
import com.hfut.stock.vo.resp.R;
import com.hfut.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 * Author:yuyang
 * Date:2024-04-25
 * Time:14:46
 */
@Service("stockService")
@Slf4j
public class StockServiceImpl implements StockService {
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @Autowired
    private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private StockBusinessMapper stockBusinessMapper;
    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private Cache<String,Object> caffeineCache;
    @Override
    public R<List<InnerMarketDomain>> innerIndexAll() {
        R<List<InnerMarketDomain>> result = (R<List<InnerMarketDomain>>)caffeineCache.get("innerMarketInfos", key -> {
            Date currentTime = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
            currentTime = DateTime.parse("2021-12-28 09:31:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            List<InnerMarketDomain> innerMarketDomainList = stockMarketIndexInfoMapper.innerIndexAll(currentTime, stockInfoConfig.getInner());
            if (CollectionUtils.isEmpty(innerMarketDomainList)) {
                return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
            }
            return R.ok(innerMarketDomainList);
        });
        return result;
    }

    @Override
    public R<List<blockMarketDomain>> blockIndexAll() {
        Date currentTime= DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        currentTime=DateTime.parse("2021-12-21 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<blockMarketDomain> blockMarketDomainList=stockBlockRtInfoMapper.blockIndexAll(currentTime);
        if (CollectionUtils.isEmpty(blockMarketDomainList)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(blockMarketDomainList);
    }

    @Override
    public R<PageResult<StockUpdownDomain>> getStockPageInfo(Integer page, Integer pageSize) {
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate= DateTime.parse("2022-07-07 14:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        PageHelper.startPage(page,pageSize);
        List<StockUpdownDomain> all=stockRtInfoMapper.getStockInfoByTime(curDate);
        if (CollectionUtils.isEmpty(all)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        PageInfo<StockUpdownDomain> pageInfo = new PageInfo<>(all);
        PageResult<StockUpdownDomain> pageResult = new PageResult<>(pageInfo);
        return R.ok(pageResult);
    }

    @Override
    public R<List<StockUpdownDomain>> getStockPageInfo4() {
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate= DateTime.parse("2022-07-07 14:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<StockUpdownDomain> all=stockRtInfoMapper.getStockInfo4ByTime(curDate);
        if (CollectionUtils.isEmpty(all)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(all);
    }

    @Override
    public R<Map<String, List>> getStockUpdownCount() {
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        dateTime= DateTime.parse("2022-01-06 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endTime = dateTime.toDate();
        Date startTime =DateTimeUtil.getOpenDate(dateTime).toDate();
        List<Map> upList=stockRtInfoMapper.getStockUpdownCount(startTime,endTime,1);
        List<Map> downList=stockRtInfoMapper.getStockUpdownCount(startTime,endTime,-1);
        Map<String, List> map = new HashMap<>();
        map.put("upList",upList);
        map.put("downList",downList);
        return R.ok(map);
    }

    @Override
    public void stockExport(HttpServletResponse response, Integer page, Integer pageSize) {
        R<PageResult<StockUpdownDomain>> stockPageInfo = getStockPageInfo(page, pageSize);
        List<StockUpdownDomain> exportList = stockPageInfo.getData().getRows();
        response.setContentType("application/vnd.ms-excel");
        //2.设置响应数据的编码格式
        response.setCharacterEncoding("utf-8");
        try {//ctrl+alt+t
            //3.设置默认的文件名称
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("股票信息表", "UTF-8");
            //设置默认文件名称：兼容一些特殊浏览器
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //4.响应excel流
            EasyExcel
                    .write(response.getOutputStream(),StockUpdownDomain.class)
                    .sheet("股票最新数据")
                    .doWrite(exportList);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("当前导出数据异常，当前页：{},每页大小：{},异常信息：{}",page,pageSize,e.getMessage());
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            R<Object> r = R.error(ResponseCode.NO_RESPONSE_DATA);
            try {
                String jsonData=new ObjectMapper().writeValueAsString(r);
                response.getWriter().write(jsonData);
            } catch (IOException ioException) {
                log.error("exportStockUpDownInfo响应错误信息失败,时间：{}",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            }
        }
    }

    @Override
    public R<Map<String, List>> stockTradeVol4InnerMarket() {
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        dateTime= DateTime.parse("2022-01-03 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endTime = dateTime.toDate();
        Date startTime =DateTimeUtil.getOpenDate(dateTime).toDate();
        DateTime preDateTime = DateTimeUtil.getPreviousTradingDay(dateTime);
        preDateTime=DateTime.parse("2022-01-02 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date preEndDate = preDateTime.toDate();
        Date preStartTime =DateTimeUtil.getOpenDate(preDateTime).toDate();
        //2.获取上证和深证的配置的大盘id
        //2.1 获取大盘的id集合
        List<String> markedIds = stockInfoConfig.getInner();
        //3.分别查询T日和T-1日的交易量数据，得到两个集合
        //3.1 查询T日大盘交易统计数据
        List<Map> data4T=stockMarketIndexInfoMapper.getStockTradeVol(markedIds,startTime,endTime);
        if (CollectionUtils.isEmpty(data4T)) {
            data4T=new ArrayList<>();
        }
        //3.2 查询T-1日大盘交易统计数据
        List<Map> data4PreT=stockMarketIndexInfoMapper.getStockTradeVol(markedIds,preStartTime,preEndDate);
        if (CollectionUtils.isEmpty(data4PreT)) {
            data4PreT=new ArrayList<>();
        }
        //4.组装响应数据
        HashMap<String, List> info = new HashMap<>();
        info.put("amtList",data4T);
        info.put("yesAmtList",data4PreT);
        //5.返回数据
        return R.ok(info);
    }

    @Override
    public R<Map<String, Object>> stockUpDownScopeCount() {
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        dateTime= DateTime.parse("2022-01-06 09:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date curDate = dateTime.toDate();
        //2.查询股票信息
        List<Map> maps=stockRtInfoMapper.getStockUpDownSectionByTime(curDate);
        List<String> upDownRange = stockInfoConfig.getUpDownRange();
        /*List<Map> sortedMaps=new ArrayList<>();
        for (String s : upDownRange) {
            Map temp=null;
            for (Map map : maps) {
                if(map.containsValue(s))
                {
                    temp=map;break;
                }
            }
            if(temp==null){
                temp=new HashMap();
                temp.put("count",0);
                temp.put("title",s);
            }
            sortedMaps.add(temp);
        }*/
        /*List<Map> sortedMaps = upDownRange.stream().map(new Function<String, Map>() {
            @Override
            public Map apply(String s) {
                Optional<Map> first = maps.stream().filter(new Predicate<Map>() {
                    @Override
                    public boolean test(Map map) {
                        if (map.containsValue(s)) return true;
                        else return false;
                    }
                }).findFirst();
                if (first.isPresent()) return first.get();
                else {
                    HashMap temp = new HashMap();
                    temp.put("count", 0);
                    temp.put("title", s);
                    return temp;
                }
            }
        }).collect(Collectors.toList());*/
        List<Map> sortedMaps  =  upDownRange.stream().map(title->{
            Map mp=null;
            Optional<Map> op = maps.stream().filter(m -> m.containsValue(title)).findFirst();
            //判断是否存在符合过滤条件的元素
            if (op.isPresent()) {
                mp=op.get();
            }else{
                mp=new HashMap();
                mp.put("count",0);
                mp.put("title",title);
            }
            return mp;
        }).collect(Collectors.toList());
        //3.组装数据
        HashMap<String, Object> mapInfo = new HashMap<>();
        //获取指定日期格式的字符串
        String curDateStr = dateTime.toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        mapInfo.put("time",curDateStr);
        mapInfo.put("infos",sortedMaps);
        //4.返回数据
        return R.ok(mapInfo);
    }

    @Override
    public R<List<Stock4MinuteDomain>> stockScreenTimeSharing(String code) {
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        dateTime= DateTime.parse("2021-12-30 14:47:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endTime = dateTime.toDate();
        Date startTime =DateTimeUtil.getOpenDate(dateTime).toDate();
        List<Stock4MinuteDomain> list=stockRtInfoMapper.getStockInfoByCodeAndDate(code,startTime,endTime);
        //判断非空处理
        if (CollectionUtils.isEmpty(list)) {
            list=new ArrayList<>();
        }
        //3.返回响应数据
        return R.ok(list);
    }

    @Override
    public R<StockSecondDetailDomain> stockScreenSecondDetail(String code) {
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        dateTime= DateTime.parse("2021-12-30 14:47:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date curTime = dateTime.toDate();
        StockSecondDetailDomain res=stockRtInfoMapper.getStockScreenSecondDetail(code,curTime);
        //判断非空处理
        if (res==null) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        //3.返回响应数据
        return R.ok(res);
    }

    @Override
    public R<List<StockScreenSecondDomain>> stockScreenSecond(String code) {
        //2.调用mapper接口获取查询的集合信息-方案1
        List<StockScreenSecondDomain> res= stockRtInfoMapper.getstockScreenSecond(code);
        //3.组装数据，响应
        if (CollectionUtils.isEmpty(res)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(res);
    }

    @Override
    public R<List<Stock4EvrDayDomain>> stockCreenDkLine(String stockCode) {
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        dateTime= DateTime.parse("2021-12-30 14:47:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endTime = dateTime.toDate();
        Date startTime =dateTime.minusDays(10).toDate();
        //2.调用mapper接口获取查询的集合信息-方案1
        //List<Stock4EvrDayDomain> data= stockRtInfoMapper.getStockInfo4EvrDay(stockCode,startTime,endTime);
        // 分步实现
        List<Date> times = stockRtInfoMapper.getLatestTime(stockCode, startTime, endTime);
        List<Stock4EvrDayDomain> data = stockRtInfoMapper.getStockInfo4EvrDayBlock(stockCode, times);
        //3.组装数据，响应
        return R.ok(data);
    }

    @Override
    public R<List<Stock4EvrWeekDomain>> stockCreenWkLine(String stockCode) {
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date endTime = dateTime.toDate();
        Date startTime =dateTime.minusYears(5).toDate();
        //2.调用mapper接口获取查询的集合信息-方案1
        List<Stock4EvrWeekDomain> data= stockRtInfoMapper.getStockInfo4EvrWeek(stockCode,startTime,endTime);
        //3.组装数据，响应
        return R.ok(data);
    }

    @Override
    public R<List<outerMarketDomain>> externalIndex4() {
        R<List<outerMarketDomain>> result = (R<List<outerMarketDomain>>)caffeineCache.get("externalIndex4Infos", key -> {
            Date currentTime = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
            currentTime = DateTime.parse("2022-05-18 15:58:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            List<outerMarketDomain> outerMarketDomainList = stockOuterMarketIndexInfoMapper.externalIndex4(currentTime,stockInfoConfig.getOuter());
            if (CollectionUtils.isEmpty(outerMarketDomainList)) {
                return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
            }
            return R.ok(outerMarketDomainList);
        });
        return result;
    }

    @Override
    public R<List<Map>> stockSearchObscure(String searchStr) {
            Date currentTime = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
//            currentTime = DateTime.parse("2021-12-19 09:47:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
           List<Map> resList =stockBusinessMapper.stockSearchObscure(searchStr.toCharArray());
            if (CollectionUtils.isEmpty(resList)) {
                return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
            }
            return R.ok(resList);
    }

    @Override
    public R<stockBusinessDomain> stockDescribe(String code) {
       stockBusinessDomain res=stockBusinessMapper.getStockDescribe(code);
       if(res==null){
           return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
       }
        return R.ok(res);
    }
}
