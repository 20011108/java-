package com.hfut.stock.vo.req;

import lombok.Data;

/**
 * Description:/api/user添加用户前端响应数据封装
 * Author:yuyang
 * Date:2024-04-30
 * Time:19:24
 */
@Data
public class SysUserAddReqVo {
    private String username;
    private String password;
    private String phone;
    private String email;
    private String nickName;
    private String realName;
    private Integer sex;
    private Integer createWhere;
    private Integer status;
}
