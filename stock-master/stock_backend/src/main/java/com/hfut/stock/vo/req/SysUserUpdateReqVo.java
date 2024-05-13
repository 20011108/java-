package com.hfut.stock.vo.req;

import lombok.Data;

/**
 * Description: put /api/user 请求数据封装
 * Author:yuyang
 * Date:2024-05-01
 * Time:15:37
 */
@Data
public class SysUserUpdateReqVo {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private String nickName;
    private String realName;
    private Integer sex;
    private Integer createWhere;
    private Integer status;
}
