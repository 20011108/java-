package com.hfut.stock.pojo.domin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description: 个股周K数据封装
 * Author:yuyang
 * Date:2024-04-29
 * Time:14:39
 */
@ApiModel(description = "Description: 个股周K数据封装 Author:yuyang Date:2024-04-29 Time:14:39")
@Data
public class Stock4EvrWeekDomain {
    //一周内平均价
    @ApiModelProperty(value = "一周内平均价", position = 1)
    private BigDecimal avgPrice;
    //一周内最低价
    @ApiModelProperty(value = "一周内最低价", position = 2)
    private BigDecimal minPrice;
    //周一开盘价
    @ApiModelProperty(value = "周一开盘价", position = 3)
    private BigDecimal openPrice;
    //一周内最高价
    @ApiModelProperty(value = "一周内最高价", position = 4)
    private BigDecimal maxPrice;
    //周五收盘价（如果当前日期不到周五，则显示最新价格）
    @ApiModelProperty(value = "周五收盘价（如果当前日期不到周五，则显示最新价格）", position = 5)
    private BigDecimal closePrice;
    //一周内最大时间
    @ApiModelProperty(value = "一周内最大时间", position = 6)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
    private Date mxTime;
    //股票编码
    @ApiModelProperty(value = "股票编码", position = 7)
    private String stockCode;
}
