package com.hfut.stock.controller;

import com.hfut.stock.annotation.MyLog;
import com.hfut.stock.pojo.domin.LoginUserDomain;
import com.hfut.stock.pojo.domin.SysUserDomain;
import com.hfut.stock.pojo.entity.SysUser;
import com.hfut.stock.service.userService;
import com.hfut.stock.vo.req.*;
import com.hfut.stock.vo.resp.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author:yuyang
 * Date:2024-04-24
 * Time:18:20
 */
@Api(value = "/api", tags = {"Description: Author:yuyang Date:2024-04-24 Time:18:20"})
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private userService userService;

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "string", name = "username", value = "", required = true)
    })
    @ApiOperation(value = "根据名字查找用户", notes = "", httpMethod = "GET")
    @ApiImplicitParam(name = "name",value = "用户名",required = true,paramType = "path")
    @GetMapping("/user/{username}")
    public SysUser selectUserInfoByName(@PathVariable("username") String name){
        return userService.selectUserInfoByName(name);
    }
    /*@ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "LoginReqVo", name = "loginReqVo", value = "", required = true)
    })
    @ApiOperation(value = "", notes = "", httpMethod = "POST")
    @PostMapping("/login")
    public R<accessTokenLoginRespVo> login(@RequestBody LoginReqVo loginReqVo){
        return userService.login(loginReqVo);
    }
    */
    @ApiOperation(value = "", notes = "", httpMethod = "GET")
    @GetMapping("/captcha")
    public R<Map> getCaptchaCode(){
        return userService.getCaptchaCode();
    }

    /**
     * 多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
     * @param reqVo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserMConditionReqVo", name = "reqVo", value = "", required = true)
    })
    @ApiOperation(value = "多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围", notes = "多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围", httpMethod = "POST")
    @PostMapping("/users")
    @MyLog("组织管理-用户管理-分页查询用户接口")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public R<PageResult<SysUserDomain>> getUserInfosByMCondition(@RequestBody UserMConditionReqVo reqVo){
        return userService.getUserInfosByMCondition(reqVo);
    }

    /**
     * 添加用户信息  服务路径：/api/user
     * @param reqVo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SysUserAddReqVo", name = "reqVo", value = "", required = true)
    })
    @ApiOperation(value = "添加用户信息  服务路径：/api/user", notes = "添加用户信息  服务路径：/api/user", httpMethod = "POST")
    @PostMapping("/user")
    public R addUser(@RequestBody SysUserAddReqVo reqVo,@RequestHeader("Authorization")String accessToken){
        return userService.addUser(reqVo,accessToken);
    }

    /**
     * 获取用户具有的角色信息，以及所有角色信息
     * @param userId
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "string", name = "userId", value = "", required = true)
    })
    @ApiOperation(value = "获取用户具有的角色信息，以及所有角色信息", notes = "获取用户具有的角色信息，以及所有角色信息", httpMethod = "GET")
    @GetMapping("/user/roles/{userId}")
    public R<Map<String, List>> getUserRolesAndAllRoles(@PathVariable String userId){
        return userService.getUserRolesAndAllRoles(userId);
    }

    /**
     * 更新用户角色信息
     * @param reqVo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UpdateUserRolesReqVo", name = "reqVo", value = "", required = true)
    })
    @ApiOperation(value = "更新用户角色信息", notes = "更新用户角色信息", httpMethod = "PUT")
    @PutMapping("/user/roles")
    public R updateUserRoles(@RequestBody UpdateUserRolesReqVo reqVo){
        return userService.updateUserRoles(reqVo);
    }

    /**
     * 批量删除用户信息，delete请求可通过请求体携带数据
     * @param userIds
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "List<Long>", name = "userIds", value = "", required = true)
    })
    @ApiOperation(value = "批量删除用户信息，delete请求可通过请求体携带数据", notes = "批量删除用户信息，delete请求可通过请求体携带数据", httpMethod = "DELETE")
    @DeleteMapping("/user")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public R deleteUsers(@RequestBody List<Long> userIds){
        return userService.deleteUsers(userIds);
    }

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "string", name = "userId", value = "", required = true)
    })
    @ApiOperation(value = "根据用户id查询用户信息", notes = "根据用户id查询用户信息", httpMethod = "GET")
    @GetMapping("/user/info/{userId}")
    @MyLog("组织管理-用户管理-用户信息详情接口")
    public R<UserInfoRespVo> selectUserInfoById(@PathVariable String userId){
        return userService.selectUserInfoById(userId);
    }

    /**
     * 根据id更新用户基本信息
     * @param reqVo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SysUserUpdateReqVo", name = "reqVo", value = "", required = true)
    })
    @ApiOperation(value = "根据id更新用户基本信息", notes = "根据id更新用户基本信息", httpMethod = "PUT")
    @PutMapping("/user")
    public  R updateUserInfo(@RequestBody SysUserUpdateReqVo reqVo,@RequestHeader("Authorization")String accessToken){
        return userService.updateUserInfo(reqVo,accessToken);
    }
}
