package com.hfut.stock.controller;

import com.hfut.stock.pojo.domin.SysPermissionDomain;
import com.hfut.stock.pojo.entity.SysPermission;
import com.hfut.stock.service.PermsService;
import com.hfut.stock.vo.req.PermissionAddVo;
import com.hfut.stock.vo.req.PermissionUpdateVo;
import com.hfut.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description:权限的控制器
 * Author:yuyang
 * Date:2024-05-02
 * Time:16:11
 */
@Api(value = "/api", tags = {"Description:权限的控制器 Author:yuyang Date:2024-05-02 Time:16:11"})
@RestController
@RequestMapping("/api")
public class PermsController {
    @Autowired
    private PermsService permsService;

    /**
     * 查询所有权限集合
     * @return
     */
    @ApiOperation(value = "查询所有权限集合", notes = "查询所有权限集合", httpMethod = "GET")
    @GetMapping("/permissions")
    public R<List<SysPermission>> getAllPerms(){
        return permsService.getAllPerms();
    }

    /**
     * 添加权限时回显权限树,仅仅显示目录和菜单
     */
    @ApiOperation(value = "添加权限时回显权限树,仅仅显示目录和菜单", notes = "添加权限时回显权限树,仅仅显示目录和菜单", httpMethod = "GET")
    @GetMapping("/permissions/tree")
    public R<List<SysPermissionDomain>> getPermsTree(){
        return permsService.getPermsTree();
    }

    /**
     * 权限添加按钮
     * @param addVo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "PermissionAddVo", name = "addVo", value = "", required = true)
    })
    @ApiOperation(value = "权限添加按钮", notes = "权限添加按钮", httpMethod = "POST")
    @PostMapping("/permission")
    public R addPerms(@RequestBody PermissionAddVo addVo){
        return permsService.addPerms(addVo);
    }

    /**
     * 更新权限
     * @param updateVo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "PermissionUpdateVo", name = "updateVo", value = "", required = true)
    })
    @ApiOperation(value = "更新权限", notes = "更新权限", httpMethod = "PUT")
    @PutMapping("/permission")
    public R updatePerms(@RequestBody PermissionUpdateVo updateVo){
        return permsService.updatePerms(updateVo);
    }

    /**
     * 删除权限
     * @param permissionId
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "string", name = "permissionId", value = "", required = true)
    })
    @ApiOperation(value = "删除权限", notes = "删除权限", httpMethod = "DELETE")
    @DeleteMapping("/permission/{permissionId}")
    public R deletePerms(@PathVariable Long permissionId){
        return permsService.deletePerms(permissionId);
    }
}
