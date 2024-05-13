package com.hfut.stock.mapper;

import com.hfut.stock.pojo.domin.stockBusinessDomain;
import com.hfut.stock.pojo.entity.StockBusiness;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author yuyang
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2024-04-24 17:56:02
* @Entity com.hfut.stock.pojo.entity.StockBusiness
*/
public interface StockBusinessMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);

    List<String> getAllStockCode();

    stockBusinessDomain getStockDescribe(@Param("code") String code);

    List<Map> stockSearchObscure(@Param("strs")char[] toCharArray);
}
