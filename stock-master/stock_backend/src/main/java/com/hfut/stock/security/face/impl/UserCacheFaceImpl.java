package com.hfut.stock.security.face.impl;

import com.hfut.stock.mapper.SysPermissionMapper;
import com.hfut.stock.mapper.SysRoleMapper;
import com.hfut.stock.pojo.domin.menusPermDomain;
import com.hfut.stock.pojo.entity.SysPermission;
import com.hfut.stock.pojo.entity.SysRole;
import com.hfut.stock.security.face.UserCacheFace;
import com.hfut.stock.security.user.LoginUserDetail;
import com.hfut.stock.utils.ParsePerm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author:yuyang
 * Date:2024-05-05
 * Time:15:57
 */
@Component("userCacheFace")
@CacheConfig(cacheNames = "user")
public class UserCacheFaceImpl implements UserCacheFace {
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Override
    @Cacheable(key="'getUserPermsById'+#uid")
    public List<SysPermission> getUserPermsById(Long uid) {
        //获取指定用户的权限集合 添加获取侧边栏数据和按钮权限的结合信息
        List<SysPermission> perms = sysPermissionMapper.getPermsByUserId(uid);
        return perms;
    }

    @Override
    @Cacheable(key="'getUserPermsAndRole'+#uid")
    public List<String> getUserPermsAndRole(List<SysPermission> perms, Long uid) {
        //5.1 获取用户拥有的角色
        List<SysRole> roles = sysRoleMapper.getRoleByUserId(uid);
        //5.2 将用户的权限标识和角色标识维护到权限集合中
        List<String> ps=new ArrayList<>();
        perms.stream().forEach(per->{
            if (StringUtils.isNotBlank(per.getPerms())) {
                ps.add(per.getPerms());
            }
        });
        roles.stream().forEach(role->{
            ps.add("ROLE_"+role.getName());
        });
        return ps;
    }

    @Override
    @Cacheable(key="'getMenus'+#uid")
    public List<menusPermDomain> getMenus(List<SysPermission> perms,Long uid) {
        //前端需要的获取树状权限菜单数据
        List<menusPermDomain> menus = ParsePerm.digui(perms, 0L);

        return menus;
    }

    @Override
    @Cacheable(key="'getPermissionsButton'+#uid")
    public List<String> getPermissionsButton(List<SysPermission> perms, Long uid) {

        //前端需要的获取菜单按钮集合
        List<String> permissions = ParsePerm.getPermissions(perms);

        return permissions;
    }
}
