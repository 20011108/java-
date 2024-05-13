package com.hfut.stock.mapper;

import com.hfut.stock.pojo.domin.*;
import com.hfut.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author yuyang
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2024-04-24 17:56:02
* @Entity com.hfut.stock.pojo.entity.StockRtInfo
*/
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    List<StockUpdownDomain> getStockInfoByTime(@Param("curDate") Date curDate);

    List<StockUpdownDomain> getStockInfo4ByTime(@Param("curDate")Date curDate);

    /**
     *
     * @param startTime
     * @param endTime
     * @param flag 1 涨停 -1 跌停
     * @return
     */
    List<Map> getStockUpdownCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("flag") int flag);

    List<Map> getStockUpDownSectionByTime(@Param("curDate") Date curDate);

    List<Stock4MinuteDomain> getStockInfoByCodeAndDate(@Param("code") String code, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<Stock4EvrDayDomain> getStockInfo4EvrDay(@Param("code") String stockCode, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 返回给定股票给定日期范围的每天的收盘时间，即最大的cur_time
     * @param stockCode
     * @param startTime
     * @param endTime
     * @return
     */
    List<Date> getLatestTime(@Param("code") String stockCode,@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 根据 getLatestTime，实现 getStockInfo4EvrDay
     * @param stockCode
     * @param times
     * @return
     */
    List<Stock4EvrDayDomain> getStockInfo4EvrDayBlock(@Param("code") String stockCode, @Param("times") List times);

    int insertBatch(@Param("infos") List<StockRtInfo> list);

    List<Stock4EvrWeekDomain> getStockInfo4EvrWeek(@Param("stockCode") String stockCode, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    StockSecondDetailDomain getStockScreenSecondDetail(@Param("code") String code, @Param("curTime") Date curTime);

    List<StockScreenSecondDomain> getstockScreenSecond(@Param("code") String code);
}
