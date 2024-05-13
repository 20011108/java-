package com.hfut.stock.mapper;

import com.hfut.stock.pojo.domin.outerMarketDomain;
import com.hfut.stock.pojo.entity.StockOuterMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author yuyang
* @description 针对表【stock_outer_market_index_info(外盘详情信息表)】的数据库操作Mapper
* @createDate 2024-04-24 17:56:02
* @Entity com.hfut.stock.pojo.entity.StockOuterMarketIndexInfo
*/
public interface StockOuterMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockOuterMarketIndexInfo record);

    int insertSelective(StockOuterMarketIndexInfo record);

    StockOuterMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockOuterMarketIndexInfo record);

    int updateByPrimaryKey(StockOuterMarketIndexInfo record);

    List<outerMarketDomain> externalIndex4(@Param("time") Date currentTime, @Param("outers") List<String> outer);

    int insertBatch(@Param("infos") List<StockOuterMarketIndexInfo> entities);
}
