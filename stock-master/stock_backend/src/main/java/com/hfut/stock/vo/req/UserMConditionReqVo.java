package com.hfut.stock.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Description:/api/users请求数据封装
 * Author:yuyang
 * Date:2024-04-30
 * Time:15:12
 */
@Data
public class UserMConditionReqVo {

    private Integer pageNum;
    private Integer pageSize;
    private String username;
    private String nickName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date endTime;
}
