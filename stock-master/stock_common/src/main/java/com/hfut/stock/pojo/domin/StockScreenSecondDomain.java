package com.hfut.stock.pojo.domin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Author:yuyang
 * Date:2024-04-29
 * Time:16:45
 */
@ApiModel(description = "Description: Author:yuyang Date:2024-04-29 Time:16:45")
@Data
public class StockScreenSecondDomain {
    //当前时间，精确到分
    @ApiModelProperty(value = "当前时间，精确到分", position = 1)
    @JsonFormat(pattern = "yyyy-MM-dd-HH:mm",timezone = "Asia/Shanghai")
    private Date date;
    //交易量
    @ApiModelProperty(value = "交易量", position = 2)
    private Long tradeAmt;
    //交易金额
    @ApiModelProperty(value = "交易金额", position = 3)
    private BigDecimal tradeVol;
    //交易价格
    @ApiModelProperty(value = "交易价格", position = 4)
    private BigDecimal tradePrice;
}
