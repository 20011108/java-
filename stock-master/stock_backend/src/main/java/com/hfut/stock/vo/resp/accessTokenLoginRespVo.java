package com.hfut.stock.vo.resp;

import com.hfut.stock.pojo.domin.menusPermDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description: 将用户的id和name信息经过base64编码，作为票据token响应给前端：
 * Author:yuyang
 * Date:2024-05-02
 * Time:17:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class accessTokenLoginRespVo {
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
    private String accessToken;
}
