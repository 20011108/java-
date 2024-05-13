package com.hfut.stock.mapper;

import com.hfut.stock.pojo.domin.SysUserDomain;
import com.hfut.stock.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author yuyang
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2024-04-24 17:56:02
* @Entity com.hfut.stock.pojo.entity.SysUser
*/
public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser selectUserInfoByName(@Param("username") String name);

    List<SysUserDomain> getUserInfosByMCondition(@Param("username") String username, @Param("nickName") String nickName, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int addUser(@Param("user") SysUser user);

    int deleteUsersById(@Param("userIds") List<Long> userIds);
}
