package com.hfut.stock.mapper;

import com.hfut.stock.pojo.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author yuyang
* @description 针对表【sys_user_role(用户角色表)】的数据库操作Mapper
* @createDate 2024-04-24 17:56:02
* @Entity com.hfut.stock.pojo.entity.SysUserRole
*/
public interface SysUserRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

    List<String> getRolesIdByUserId(String userId);

    int deleteUserRolesByUid(Long userId);

    int updateUserRoles(@Param("roles") List<SysUserRole> roles);
}
