package com.hfut.stock.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author by itheima
 * @Date 2021/12/30
 * @Description stock常量数据
 */
@ApiModel(description = "stock常量数据")
@ConfigurationProperties(prefix = "stock")
@Data
public class StockInfoConfig {
    //A股大盘ID集合
    @ApiModelProperty(value = "A股大盘ID集合", position = 1)
    private List<String> inner;
    //外盘ID集合
    @ApiModelProperty(value = "外盘ID集合", position = 2)
    private List<String> outer;
    //股票区间
    @ApiModelProperty(value = "股票区间", position = 3)
    private List<String> upDownRange;
    //大盘参数获取url
    @ApiModelProperty(value = "大盘参数获取url", position = 4)
    private String marketUrl;
    //板块参数获取url
    @ApiModelProperty(value = "板块参数获取url", position = 5)
    private String blockUrl;
}