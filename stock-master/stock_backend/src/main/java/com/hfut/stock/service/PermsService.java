package com.hfut.stock.service;

import com.hfut.stock.pojo.domin.SysPermissionDomain;
import com.hfut.stock.pojo.entity.SysPermission;
import com.hfut.stock.vo.req.PermissionAddVo;
import com.hfut.stock.vo.req.PermissionUpdateVo;
import com.hfut.stock.vo.resp.R;

import java.util.List;

/**
 * Description:
 * Author:yuyang
 * Date:2024-05-02
 * Time:16:12
 */
public interface PermsService {
    R<List<SysPermission>> getAllPerms();

    R<List<SysPermissionDomain>> getPermsTree();

    R addPerms(PermissionAddVo addVo);

    R updatePerms(PermissionUpdateVo updateVo);

    R deletePerms(Long permissionId);
}
