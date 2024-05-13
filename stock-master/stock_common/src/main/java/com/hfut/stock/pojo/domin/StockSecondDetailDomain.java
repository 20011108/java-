package com.hfut.stock.pojo.domin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description: 个股最新分时行情数据封装
 * Author:yuyang
 * Date:2024-04-29
 * Time:16:28
 */
@Data
public class StockSecondDetailDomain {
    /**
     * 日期，eg:202201280809
     */
    @ApiModelProperty(value = "日期，eg:202201280809", position = 1)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
    private Date curDate;
    /**
     * 交易量
     */
    @ApiModelProperty(value = "交易量", position = 2)
    private Long tradeAmt;
    /**
     * 最低价
     */
    @ApiModelProperty(value = "最低价", position = 4)
    private BigDecimal lowPrice;
    /**
     * 前收盘价
     */
    @ApiModelProperty(value = "前收盘价", position = 5)
    private BigDecimal preClosePrice;
    /**
     * 最高价
     */
    @ApiModelProperty(value = "最高价", position = 7)
    private BigDecimal highPrice;
    /**
     * 开盘价
     */
    @ApiModelProperty(value = "开盘价", position = 8)
    private BigDecimal openPrice;

    /**
     * 当前交易总金额
     */
    @ApiModelProperty(value = "当前交易总金额", position = 9)
    private BigDecimal tradeVol;
    /**
     * 当前价格
     */
    @ApiModelProperty(value = "当前价格", position = 10)
    private BigDecimal tradePrice;
}
