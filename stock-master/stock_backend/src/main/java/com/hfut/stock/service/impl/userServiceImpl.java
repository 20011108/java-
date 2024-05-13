package com.hfut.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.io.BaseEncoding;
import com.hfut.stock.constan.StockConstant;
import com.hfut.stock.mapper.SysPermissionMapper;
import com.hfut.stock.mapper.SysRoleMapper;
import com.hfut.stock.mapper.SysUserMapper;
import com.hfut.stock.mapper.SysUserRoleMapper;
import com.hfut.stock.pojo.domin.LoginUserDomain;
import com.hfut.stock.pojo.domin.SysUserDomain;
import com.hfut.stock.pojo.entity.SysPermission;
import com.hfut.stock.pojo.entity.SysRole;
import com.hfut.stock.pojo.entity.SysUser;
import com.hfut.stock.pojo.entity.SysUserRole;
import com.hfut.stock.service.userService;
import com.hfut.stock.utils.IdWorker;
import com.hfut.stock.utils.ParsePerm;
import com.hfut.stock.vo.req.*;
import com.hfut.stock.vo.resp.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Author:yuyang
 * Date:2024-04-24
 * Time:18:19
 */
@Service("userService")
@Slf4j
public class userServiceImpl implements userService {
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    SysPermissionMapper sysPermissionMapper;
    @Autowired
    SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    SysRoleMapper sysRoleMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    IdWorker idWorker;
    @Override
    public SysUser selectUserInfoByName(String name) {
        return sysUserMapper.selectUserInfoByName(name);
    }

    @Override
    public R<accessTokenLoginRespVo> login(LoginReqVo vo) {
        if(vo==null|| StringUtils.isBlank(vo.getUsername())|| StringUtils.isBlank(vo.getPassword())){
            return R.error(ResponseCode.DATA_ERROR);
        }
        //2.校验验证码和sessionId是否有效
        if (StringUtils.isBlank(vo.getCode()) || StringUtils.isBlank(vo.getSessionId())){
            return R.error(ResponseCode.DATA_ERROR);
        }
        //3.根据Rkey从redis中获取缓存的校验码
        String rCode= (String) redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX+vo.getSessionId());
        //判断获取的验证码是否存在，以及是否与输入的验证码相同
        if (StringUtils.isBlank(rCode) || ! rCode.equalsIgnoreCase(vo.getCode())) {
            //验证码输入有误
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        SysUser dbUser = sysUserMapper.selectUserInfoByName(vo.getUsername());
        if (dbUser==null||!passwordEncoder.matches(vo.getPassword(),dbUser.getPassword()))
        {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
        LoginUserDomain loginUserDomain = new LoginUserDomain();
        BeanUtils.copyProperties(dbUser,loginUserDomain);
        List<SysPermission> perms = sysPermissionMapper.getPermsByUserId(dbUser.getId());
        loginUserDomain.setPermissions(ParsePerm.getPermissions(perms));
        loginUserDomain.setMenus(ParsePerm.digui(perms,0L));
        accessTokenLoginRespVo accessTokenLoginRespVo = new accessTokenLoginRespVo();
        BeanUtils.copyProperties(loginUserDomain,accessTokenLoginRespVo);
        String info=accessTokenLoginRespVo.getId()+":"+ accessTokenLoginRespVo.getUsername();
        accessTokenLoginRespVo.setAccessToken(BaseEncoding.base64().encode(info.getBytes()));
        return R.ok(accessTokenLoginRespVo);
    }

    @Override
    public R<Map> getCaptchaCode() {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(250, 40, 4, 5);
        lineCaptcha.setBackground(Color.LIGHT_GRAY);
        String code = lineCaptcha.getCode();
        String imageBase64 = lineCaptcha.getImageBase64();
        String sessionId = String.valueOf(idWorker.nextId());
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX+sessionId,code,5, TimeUnit.MINUTES);// 设置到期时间
        log.info(code+"---"+sessionId);
        HashMap<String, String> map = new HashMap<>();
        map.put("sessionId",sessionId);
        map.put("imageData",imageBase64);//获取base64格式的图片数据
        return R.ok(map);
    }

    @Override
    public R<PageResult<SysUserDomain>> getUserInfosByMCondition(UserMConditionReqVo reqVo) {
        PageHelper.startPage(reqVo.getPageNum(),reqVo.getPageSize());
        List<SysUserDomain> all=sysUserMapper.getUserInfosByMCondition(reqVo.getUsername(),reqVo.getNickName(),reqVo.getStartTime(),reqVo.getEndTime());
        if (CollectionUtils.isEmpty(all)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        PageInfo<SysUserDomain> pageInfo = new PageInfo<>(all);
        PageResult<SysUserDomain> pageResult = new PageResult<>(pageInfo);
        return R.ok(pageResult);
    }

    @Override
    public R addUser(SysUserAddReqVo reqVo,String accessToken) {
        String info = new String(BaseEncoding.base64().decode(accessToken));
        SysUser dbUser = sysUserMapper.selectUserInfoByName(reqVo.getUsername());
        if (dbUser!=null)
        {
            return R.error(ResponseCode.ACCOUNT_EXISTS_ERROR);
        }
        SysUser user = new SysUser();
        BeanUtils.copyProperties(reqVo,user);
        user.setId(idWorker.nextId());
        user.setCreateId(Long.parseLong(info.split(":")[0]));
        int i=sysUserMapper.addUser(user);
        if(i>0){
            return R.ok();
        }
        else
            return R.error(ResponseCode.ERROR.getMessage());
    }

    @Override
    public R<Map<String, List>> getUserRolesAndAllRoles(String userId) {
        HashMap<String, List> map = new HashMap<>();
        List<String> userRolesIds=sysUserRoleMapper.getRolesIdByUserId(userId);
        List<SysRole> roles=sysRoleMapper.selectAll();
        if (CollectionUtils.isEmpty(roles)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        map.put("ownRoleIds",userRolesIds);
        map.put("allRole",roles);
        return R.ok(map);
    }

    @Override
    public R updateUserRoles(UpdateUserRolesReqVo reqVo) {
        List<String> rolesList = sysUserRoleMapper.getRolesIdByUserId(String.valueOf(reqVo.getUserId()));
        if(!CollectionUtils.isEmpty(rolesList)){
             int i=sysUserRoleMapper.deleteUserRolesByUid(reqVo.getUserId());
             if(i<=0){
                 return R.error(ResponseCode.DATA_ERROR.getMessage());
             }
        }
        List<SysUserRole> userRoles = new ArrayList<>();
        for (Long roleId : reqVo.getRoleIds()) {
            userRoles.add(SysUserRole.builder().id(idWorker.nextId()).userId(reqVo.getUserId())
                    .roleId(roleId).build());
        }
        int j=sysUserRoleMapper.updateUserRoles(userRoles);
        if(j>0){
            return R.ok();
        }
        else return R.error(ResponseCode.DATA_ERROR.getMessage());
    }

    @Override
    public R deleteUsers(List<Long> userIds) {
        int i=sysUserMapper.deleteUsersById(userIds);
        if(i>0){
            return R.ok();
        }
        else
            return R.error(ResponseCode.ERROR.getMessage());
    }

    @Override
    public R<UserInfoRespVo> selectUserInfoById(String id) {
        SysUser dbUser=sysUserMapper.selectByPrimaryKey(Long.parseLong(id));
        if(dbUser!=null){
            UserInfoRespVo respVo = new UserInfoRespVo();
            BeanUtils.copyProperties(dbUser,respVo);
            return R.ok(respVo);
        }else {
            return R.error(ResponseCode.ERROR.getMessage());
        }

    }

    @Override
    public R updateUserInfo(SysUserUpdateReqVo reqVo,String accessToken) {
        String info = new String(BaseEncoding.base64().decode(accessToken));
        SysUser dbUser = new SysUser();
        BeanUtils.copyProperties(reqVo,dbUser);
        dbUser.setUpdateId(Long.parseLong(info.split(":")[0]));
        int i=sysUserMapper.updateByPrimaryKeySelective(dbUser);
        if(i>0){
            return R.ok();
        }
        else
            return R.error(ResponseCode.ERROR.getMessage());
    }
}
