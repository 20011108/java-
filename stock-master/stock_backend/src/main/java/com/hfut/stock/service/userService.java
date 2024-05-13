package com.hfut.stock.service;

import com.hfut.stock.pojo.domin.LoginUserDomain;
import com.hfut.stock.pojo.domin.SysUserDomain;
import com.hfut.stock.pojo.entity.SysUser;
import com.hfut.stock.vo.req.*;
import com.hfut.stock.vo.resp.PageResult;
import com.hfut.stock.vo.resp.R;
import com.hfut.stock.vo.resp.UserInfoRespVo;
import com.hfut.stock.vo.resp.accessTokenLoginRespVo;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author:yuyang
 * Date:2024-04-24
 * Time:18:18
 */
public interface userService {
    SysUser selectUserInfoByName(String name);

    R<accessTokenLoginRespVo> login(LoginReqVo loginReqVo);

    R<Map> getCaptchaCode();

    R<PageResult<SysUserDomain>> getUserInfosByMCondition(UserMConditionReqVo reqVo);

    R addUser(SysUserAddReqVo reqVo,String accessToken);

    R<Map<String, List>> getUserRolesAndAllRoles(String userId);

    R updateUserRoles(UpdateUserRolesReqVo reqVo);

    R deleteUsers(List<Long> userIds);

    R<UserInfoRespVo> selectUserInfoById(String id);

    R updateUserInfo(SysUserUpdateReqVo reqVo,String accessToken);
}
