package com.hfut.stock.mapper;

import com.hfut.stock.pojo.entity.SysRolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author yuyang
* @description 针对表【sys_role_permission(角色权限表)】的数据库操作Mapper
* @createDate 2024-04-24 17:56:02
* @Entity com.hfut.stock.pojo.entity.SysRolePermission
*/
public interface SysRolePermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

    int insertPerms(@Param("perms") List<SysRolePermission> list);

    List<Long> getPermsByRoleId(@Param("roleId") String roleId);

    int deleteByRoleId(@Param("id") Long id);

    /**
     * 根据权限id删除关联的角色信息
     * @param permissionId
     * @return
     */
    int deleteByPermissionId(@Param("permissionId") Long permissionId);
}
