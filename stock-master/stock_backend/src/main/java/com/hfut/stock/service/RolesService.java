package com.hfut.stock.service;

import com.hfut.stock.pojo.domin.menusPermDomain;
import com.hfut.stock.pojo.entity.SysRole;
import com.hfut.stock.vo.req.addRolesAndPermsReqVo;
import com.hfut.stock.vo.req.updatePermsByRoleIdReqVo;
import com.hfut.stock.vo.resp.PageResult;
import com.hfut.stock.vo.resp.R;

import java.util.List;

/**
 * Description:
 * Author:yuyang
 * Date:2024-05-02
 * Time:13:56
 */
public interface RolesService {
    R<PageResult<SysRole>> getRolesPageInfo(Integer pageNum, Integer pageSize);

    R<List<menusPermDomain>> getPermissionsTreeAll();

    R addRolesAndPerms(addRolesAndPermsReqVo reqVo);

    R<List<Long>> getPermsByRoleId(String roleId);

    R updatePermsByRoleId(updatePermsByRoleIdReqVo reqVo);

    R deleteRoles(String roleId);

    R updateRoleStatus(String roleId, Integer status);
}
