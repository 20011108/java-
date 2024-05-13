package com.hfut.stock.vo.req;

import lombok.Data;

import java.util.List;

/**
 * Description:添加角色和角色关联权限请求数据封装
 * Author:yuyang
 * Date:2024-05-02
 * Time:15:04
 */
@Data
public class addRolesAndPermsReqVo {
    private String name;
    private String description;
    private List<Long> permissionsIds;
}
