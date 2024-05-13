package com.hfut.stock.vo.req;

import lombok.Data;

import java.util.List;

/**
 * Description:添加角色和角色关联权限请求数据封装
 * Author:yuyang
 * Date:2024-05-02
 * Time:15:41
 */
@Data
public class updatePermsByRoleIdReqVo {
    private Long id;
    private String name;
    private String description;
    private List<Long> permissionsIds;
}
