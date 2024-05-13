package com.hfut.stock.mapper;

import com.hfut.stock.pojo.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author yuyang
* @description 针对表【sys_permission(权限表（菜单）)】的数据库操作Mapper
* @createDate 2024-04-24 17:56:02
* @Entity com.hfut.stock.pojo.entity.SysPermission
*/
public interface SysPermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    List<SysPermission> getPermsByUserId(@Param("uid") Long uid);

    List<SysPermission> selectAll();

    List<SysPermission> selectByType(@Param("typeId") Integer typeId);

    int addPerms(@Param("addPerms") SysPermission addPerms);

    int updatePerms(@Param("addPerms") SysPermission addPerms);

    int deletePerms(@Param("permissionId") String permissionId);

    /**
     * 根据权限父类id查询对应子集权限
     * @param permissionId
     * @return
     */
    int findChildrenCountByParentId(@Param("permissionId") Long permissionId);
}
