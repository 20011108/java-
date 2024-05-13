package com.hfut.stock.pojo.domin;

import com.hfut.stock.pojo.entity.SysPermission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:添加权限时回显权限树,仅仅显示目录和菜单 的数据封装
 * Author:yuyang
 * Date:2024-05-02
 * Time:16:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysPermissionDomain {
     private Long id;
     private String title;
     private Integer level;
     public SysPermissionDomain(SysPermission sysPermission){
         id=sysPermission.getId();
         title=sysPermission.getTitle();
         level=sysPermission.getType();
     }
}
