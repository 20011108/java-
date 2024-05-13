package com.hfut.stock.mapper;

import com.hfut.stock.pojo.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author yuyang
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2024-04-24 17:56:02
* @Entity com.hfut.stock.pojo.entity.SysRole
*/
public interface SysRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<SysRole> selectAll();

    int addRole(@Param("role") SysRole role);

    int deleteRole(@Param("roleId") String roleId);

    int updateRoleStatus(@Param("roleId") String roleId, @Param("status") Integer status);

    List<SysRole> getRoleByUserId(@Param("uid") Long id);
}
