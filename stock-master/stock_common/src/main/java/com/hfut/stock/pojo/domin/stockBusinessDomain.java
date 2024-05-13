package com.hfut.stock.pojo.domin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description: stockbusiness表的查询数据封装
 * Author:yuyang
 * Date:2024-04-29
 * Time:11:21
 */
@ApiModel(description = "Description: stockbusiness表的查询数据封装 Author:yuyang Date:2024-04-29 Time:11:21")
@Data
public class stockBusinessDomain {
    //股票编码
    @ApiModelProperty(value = "股票编码", position = 1)
    private String code;
    //行业，也就是行业板块名称
    @ApiModelProperty(value = "行业，也就是行业板块名称", position = 2)
    private String trade;
    //公司主营业务
    @ApiModelProperty(value = "公司主营业务", position = 3)
    private String business;
    //公司名称
    @ApiModelProperty(value = "公司名称", position = 4)
    private String name;
}
