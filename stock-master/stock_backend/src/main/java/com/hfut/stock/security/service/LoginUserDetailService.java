package com.hfut.stock.security.service;

import com.google.common.io.BaseEncoding;
import com.hfut.stock.mapper.SysPermissionMapper;
import com.hfut.stock.mapper.SysRoleMapper;
import com.hfut.stock.mapper.SysUserMapper;
import com.hfut.stock.pojo.domin.LoginUserDomain;
import com.hfut.stock.pojo.domin.menusPermDomain;
import com.hfut.stock.pojo.entity.SysPermission;
import com.hfut.stock.pojo.entity.SysRole;
import com.hfut.stock.pojo.entity.SysUser;
import com.hfut.stock.security.face.UserCacheFace;
import com.hfut.stock.security.user.LoginUserDetail;
import com.hfut.stock.utils.ParsePerm;
import com.hfut.stock.vo.resp.R;
import com.hfut.stock.vo.resp.ResponseCode;
import com.hfut.stock.vo.resp.accessTokenLoginRespVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:定义获取用户合法详情信息的服务
 * Author:yuyang
 * Date:2024-05-04
 * Time:16:02
 */
@Service("userDetailsService")
public class LoginUserDetailService implements UserDetailsService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private UserCacheFace userCacheFace;
    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        SysUser dbUser = sysUserMapper.selectUserInfoByName(loginName);
        if (dbUser==null)
        {
            throw new UsernameNotFoundException("用户不存在");
        }
        //获取指定用户的权限集合 添加获取侧边栏数据和按钮权限的结合信息
//        List<SysPermission> perms = sysPermissionMapper.getPermsByUserId(dbUser.getId());
        List<SysPermission> perms=userCacheFace.getUserPermsById(dbUser.getId());
        //前端需要的获取菜单按钮集合
//        List<String> permissions = ParsePerm.getPermissions(perms);
        List<String> permissions =userCacheFace.getPermissionsButton(perms, dbUser.getId());
                //前端需要的获取树状权限菜单数据
//        List<menusPermDomain> menus = ParsePerm.digui(perms, 0L);
        List<menusPermDomain> menus =userCacheFace.getMenus(perms,dbUser.getId());
        //5.组装后端需要的权限标识
        //5.1 获取用户拥有的角色
//        List<SysRole> roles = sysRoleMapper.getRoleByUserId(dbUser.getId());
//        //5.2 将用户的权限标识和角色标识维护到权限集合中
//        List<String> ps=new ArrayList<>();
//        perms.stream().forEach(per->{
//            if (StringUtils.isNotBlank(per.getPerms())) {
//                ps.add(per.getPerms());
//            }
//        });
//        roles.stream().forEach(role->{
//            ps.add("ROLE_"+role.getName());
//        });
        List<String> ps=userCacheFace.getUserPermsAndRole(perms, dbUser.getId());
        String[] permStr=ps.toArray(new String[ps.size()]);
        //5.3 将用户权限标识转化成权限对象集合
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(permStr);
        LoginUserDetail loginUserDetail = new LoginUserDetail();
        BeanUtils.copyProperties(dbUser,loginUserDetail);
        loginUserDetail.setMenus(menus);
        loginUserDetail.setAuthorities(authorityList);
        loginUserDetail.setPermissions(permissions);
        return loginUserDetail;
    }
}
