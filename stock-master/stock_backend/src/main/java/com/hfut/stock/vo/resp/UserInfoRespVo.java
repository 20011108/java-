package com.hfut.stock.vo.resp;

import lombok.Data;

/**
 * Description:/api/user/info/{userId} 响应数据封装
 * Author:yuyang
 * Date:2024-05-01
 * Time:15:21
 */
@Data
public class UserInfoRespVo {
    private Long id;
    private String username;
    private String phone;
    private String nickName;
    private String realName;
    private Integer sex;
    private Integer status;
    private String email;
}
