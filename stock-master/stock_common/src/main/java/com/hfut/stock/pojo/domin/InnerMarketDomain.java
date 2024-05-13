package com.hfut.stock.pojo.domin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author by itheima
 * @Date 2022/1/9
 * @Description 定义封装多内大盘数据的实体类
 */
@ApiModel(description = "定义封装多内大盘数据的实体类")
@Data
public class InnerMarketDomain {
    /**
     * 大盘编码
     */
    @ApiModelProperty(value = "大盘编码", position = 1)
    private String code;
    /**
     * 大盘名称
     */
    @ApiModelProperty(value = "大盘名称", position = 2)
    private String name;
    /**
     * 开盘点
     */
    @ApiModelProperty(value = "开盘点", position = 3)
    private BigDecimal openPoint;
    /**
     * 当前点
     */
    @ApiModelProperty(value = "当前点", position = 4)
    private BigDecimal curPoint;
    /**
     * 前收盘点
     */
    @ApiModelProperty(value = "前收盘点", position = 5)
    private BigDecimal preClosePoint;
    /**
     * 交易量
     */
    @ApiModelProperty(value = "交易量", position = 6)
    private Long tradeAmt;
    /**
     * 交易金额
     */
    @ApiModelProperty(value = "交易金额", position = 7)
    private Long tradeVol;
    /**
     * 涨跌值
     */
    @ApiModelProperty(value = "涨跌值", position = 8)
    private BigDecimal upDown;
    /**
     * 涨幅
     */
    @ApiModelProperty(value = "涨幅", position = 9)
    private BigDecimal rose;

    /**
     * 振幅
     */
    @ApiModelProperty(value = "振幅", position = 10)
    private BigDecimal amplitude;
    /**
     * 当前时间
     */
    @ApiModelProperty(value = "当前时间", position = 11)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date curTime;
}