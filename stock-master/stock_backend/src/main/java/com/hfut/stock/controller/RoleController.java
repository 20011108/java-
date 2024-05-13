package com.hfut.stock.controller;

import com.hfut.stock.pojo.domin.menusPermDomain;
import com.hfut.stock.pojo.entity.SysRole;
import com.hfut.stock.service.RolesService;
import com.hfut.stock.vo.req.addRolesAndPermsReqVo;
import com.hfut.stock.vo.req.updatePermsByRoleIdReqVo;
import com.hfut.stock.vo.resp.PageResult;
import com.hfut.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Description:角色处理器
 * Author:yuyang
 * Date:2024-05-02
 * Time:13:49
 */
@Api(value = "/api", tags = {"Description:角色处理器 Author:yuyang Date:2024-05-02 Time:13:49"})
@RestController
@RequestMapping("/api")
public class RoleController {
    @Autowired
    private RolesService rolesService;

    /**
     * 分页查询当前角色信息
     * @param map
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "Map<String, Integer>", name = "map", value = "", required = true)
    })
    @ApiOperation(value = "分页查询当前角色信息", notes = "分页查询当前角色信息", httpMethod = "POST")
    @PostMapping("/roles")
    public R<PageResult<SysRole>> getRolesPageInfo(@RequestBody Map<String,Integer> map) {
        return rolesService.getRolesPageInfo(map.get("pageNum"), map.get("pageSize"));
    }

    /**
     * 树状结构回显权限集合,底层通过递归获取权限数据集合
     */
    @ApiOperation(value = "树状结构回显权限集合,底层通过递归获取权限数据集合", notes = "树状结构回显权限集合,底层通过递归获取权限数据集合", httpMethod = "GET")
    @GetMapping("/permissions/tree/all")
    public R<List<menusPermDomain>> getPermissionsTreeAll() {
        return rolesService.getPermissionsTreeAll();
    }
    /**
     * 添加角色和角色关联权限
     * @param reqVo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "addRolesAndPermsReqVo", name = "reqVo", value = "", required = true)
    })
    @ApiOperation(value = "添加角色和角色关联权限", notes = "添加角色和角色关联权限", httpMethod = "POST")
    @PostMapping("/role")
    public R addRolesAndPerms(@RequestBody addRolesAndPermsReqVo reqVo) {
        return rolesService.addRolesAndPerms(reqVo);
    }

    /**
     * 根据角色id查询权限
     * @param roleId
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "string", name = "roleId", value = "", required = true)
    })
    @ApiOperation(value = "根据角色id查询权限", notes = "根据角色id查询权限", httpMethod = "GET")
    @GetMapping("/role/{roleId}")
    public R<List<Long>> getPermsByRoleId(@PathVariable String roleId){
        return rolesService.getPermsByRoleId(roleId);
    }

    /**
     * 添加角色和角色关联权限
     * @param reqVo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "updatePermsByRoleIdReqVo", name = "reqVo", value = "", required = true)
    })
    @ApiOperation(value = "添加角色和角色关联权限", notes = "添加角色和角色关联权限", httpMethod = "PUT")
    @PutMapping("/role")
    public R updatePermsByRoleId(@RequestBody updatePermsByRoleIdReqVo reqVo){
        return rolesService.updatePermsByRoleId(reqVo);
    }

    /**
     * 根据角色id删除角色信息
     * @param roleId
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "string", name = "roleId", value = "", required = true)
    })
    @ApiOperation(value = "根据角色id删除角色信息", notes = "根据角色id删除角色信息", httpMethod = "DELETE")
    @DeleteMapping("/role/{roleId}")
    public R deleteRoles(@PathVariable String roleId){
        return rolesService.deleteRoles(roleId);
    }

    /**
     * 更新用户的状态信息
     * @param roleId
     * @param status
     * @return
     */
    @PostMapping("/role/{roleId}/{status}")
    public R updateRoleStatus(@PathVariable String roleId,@PathVariable Integer status){
        return rolesService.updateRoleStatus(roleId,status);
    }
}
