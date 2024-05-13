package com.hfut.stock.vo.req;

import lombok.Data;

import java.util.List;

/**
 * Description: 更新用户角色信息前端请求数据封装
 * Author:yuyang
 * Date:2024-05-01
 * Time:10:05
 */
@Data
public class UpdateUserRolesReqVo {
    private Long userId;
    private List<Long> roleIds;
}
