package com.hfut.stock.pojo.domin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description: 股票板块domain
 * Author:yuyang
 * Date:2024-04-25
 * Time:15:14
 */
@ApiModel(description = "Description: 股票板块domain Author:yuyang Date:2024-04-25 Time:15:14")
@Data
public class blockMarketDomain {
    /**
     * 公司数量
     */
    @ApiModelProperty(value = "公司数量", position = 1)
    private Integer companyNum;
    /**
     * 交易量
     */
    @ApiModelProperty(value = "交易量", position = 2)
    private Long tradeAmt;
    /**
     * 板块编码
     */
    @ApiModelProperty(value = "板块编码", position = 3)
    private String code;
    /**
     * 平均价
     */
    @ApiModelProperty(value = "平均价", position = 4)
    private BigDecimal avgPrice;
    /**
     * 板块名称
     */
    @ApiModelProperty(value = "板块名称", position = 5)
    private String name;
    /**
     * 当前日期
     */
    @ApiModelProperty(value = "当前日期", position = 6)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date curDate;
    /**
     *交易金额
     */
    @ApiModelProperty(value = "交易金额", position = 7)
    private BigDecimal tradeVol;
    /**
     * 涨跌率
     */
    @ApiModelProperty(value = "涨跌率", position = 8)
    private BigDecimal updownRate;
}

