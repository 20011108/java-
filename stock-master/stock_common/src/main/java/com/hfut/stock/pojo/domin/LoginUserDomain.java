package com.hfut.stock.pojo.domin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description: 完善用户权限相关的信息
 * Author:yuyang
 * Date:2024-04-30
 * Time:10:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUserDomain {
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 电话
     */
    private String phone;
    /**
     * 昵称
     */
    private String nickName;
    //真实名称
     private String realName;
    //性别
     private Integer sex;
    //状态
     private Integer status;
    //邮件
     private String email;
    //侧边栏权限树（不包含按钮权限）
     private List<menusPermDomain> menus;
    //按钮权限标识
     private List<String> permissions;
}
