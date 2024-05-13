package com.hfut.stock.security.face;

import com.hfut.stock.pojo.domin.menusPermDomain;
import com.hfut.stock.pojo.entity.SysPermission;
import com.hfut.stock.security.user.LoginUserDetail;

import java.util.List;

/**
 * Description:
 * Author:yuyang
 * Date:2024-05-05
 * Time:15:52
 */
public interface UserCacheFace {
    /**
     * 获取指定用户的权限集合
     * @param uid
     */
    public List<SysPermission> getUserPermsById(Long uid);
    /**
     * 缓存用户权限信息
     * @param uid
     * @return
     */
    public List<String> getUserPermsAndRole(List<SysPermission> perms,Long uid);

    /**
     * 用户前端侧边栏信息缓存
     * @param perms
     * @return
     */
    public List<menusPermDomain> getMenus(List<SysPermission> perms, Long uid);

    /**
     * 用户关联的按钮权限信息缓存
     * @param perms
     * @return
     */
    public List<String> getPermissionsButton(List<SysPermission> perms, Long uid);
}
