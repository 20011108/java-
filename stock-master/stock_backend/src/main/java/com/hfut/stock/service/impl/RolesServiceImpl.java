package com.hfut.stock.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hfut.stock.mapper.SysPermissionMapper;
import com.hfut.stock.mapper.SysRoleMapper;
import com.hfut.stock.mapper.SysRolePermissionMapper;
import com.hfut.stock.pojo.domin.menusPermDomain;
import com.hfut.stock.pojo.entity.SysPermission;
import com.hfut.stock.pojo.entity.SysRole;
import com.hfut.stock.pojo.entity.SysRolePermission;
import com.hfut.stock.service.RolesService;
import com.hfut.stock.utils.IdWorker;
import com.hfut.stock.utils.ParsePerm;
import com.hfut.stock.vo.req.addRolesAndPermsReqVo;
import com.hfut.stock.vo.req.updatePermsByRoleIdReqVo;
import com.hfut.stock.vo.resp.PageResult;
import com.hfut.stock.vo.resp.R;
import com.hfut.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description:角色业务层
 * Author:yuyang
 * Date:2024-05-02
 * Time:13:56
 */
@Service
@Slf4j
public class RolesServiceImpl implements RolesService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Autowired
    private IdWorker idWorker;
    @Override
    public R<PageResult<SysRole>> getRolesPageInfo(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<SysRole> roleList = sysRoleMapper.selectAll();
        if (CollectionUtils.isEmpty(roleList)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        PageInfo<SysRole> pageInfo = new PageInfo<>(roleList);
        PageResult<SysRole> result = new PageResult<>(pageInfo);
        return R.ok(result);
    }

    @Override
    public R<List<menusPermDomain>> getPermissionsTreeAll() {
        List<SysPermission> permissionList=sysPermissionMapper.selectAll();
        if (CollectionUtils.isEmpty(permissionList)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(ParsePerm.digui(permissionList,0L));
    }

    @Override
    public R addRolesAndPerms(addRolesAndPermsReqVo reqVo) {
        SysRole role = SysRole.builder().id(idWorker.nextId()).name(reqVo.getName()).description(reqVo.getDescription()).build();
        int i=sysRoleMapper.addRole(role);
        if(i>0){
            List<SysRolePermission> list = new ArrayList<>();
            for (Long permissionsId : reqVo.getPermissionsIds()) {
                list.add(SysRolePermission.builder().id(idWorker.nextId()).roleId(role.getId()).permissionId(permissionsId).build());
            }
            int j=sysRolePermissionMapper.insertPerms(list);
            if(j>0){
                return R.ok("操作成功");
            }
        }
        return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
    }

    @Override
    public R<List<Long>> getPermsByRoleId(String roleId) {
        List<Long> perms=sysRolePermissionMapper.getPermsByRoleId(roleId);
        if (perms!=null){
            return R.ok(perms);
        }
        return R.error(ResponseCode.DATA_ERROR.getMessage());
    }

    @Override
    public R updatePermsByRoleId(updatePermsByRoleIdReqVo reqVo) {
        List<Long> perms = sysRolePermissionMapper.getPermsByRoleId(String.valueOf(reqVo.getId()));
        if(!CollectionUtils.isEmpty(perms)){
            int i=sysRolePermissionMapper.deleteByRoleId(reqVo.getId());
            if(i<=0){
                return R.error(ResponseCode.DATA_ERROR.getMessage());
            }
        }
        List<SysRolePermission> rolePermissions = new ArrayList<>();
        for (Long permissionsId : reqVo.getPermissionsIds()) {
            rolePermissions.add(SysRolePermission.builder().id(idWorker.nextId()).roleId(reqVo.getId())
                    .permissionId(permissionsId).build());
        }
        int j=sysRolePermissionMapper.insertPerms(rolePermissions);
        if(j>0){
            return R.ok("操作成功");
        }
        else return R.error(ResponseCode.DATA_ERROR.getMessage());
    }

    @Override
    public R deleteRoles(String roleId) {
       int i=sysRoleMapper.deleteRole(roleId);
        if(i>0){
            return R.ok("删除成功");
        }
        else return R.error(ResponseCode.DATA_ERROR.getMessage());
    }

    @Override
    public R updateRoleStatus(String roleId, Integer status) {
        int i=sysRoleMapper.updateRoleStatus(roleId,status);
        if(i>0){
            return R.ok("操作成功");
        }
        else return R.error(ResponseCode.DATA_ERROR.getMessage());
    }
}
