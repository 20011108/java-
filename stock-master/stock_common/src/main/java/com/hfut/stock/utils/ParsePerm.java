package com.hfut.stock.utils;

import com.google.common.base.Strings;
import com.hfut.stock.pojo.domin.menusPermDomain;
import com.hfut.stock.pojo.entity.SysPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Author:yuyang
 * Date:2024-04-30
 * Time:11:28
 */
public class ParsePerm {
    public static  List<String> getPermissions(List<SysPermission> permsByUserId){
        List<String> permissions=new ArrayList<>();
        /*for (SysPermission sysPermission : permsByUserId) {
            if(sysPermission.getType().equals(0003))
                permissions.add(sysPermission.getCode());
        }*/
        permissions=permsByUserId.stream()
                .filter(per -> !Strings.isNullOrEmpty(per.getCode()) && per.getType() == 3)
                .map(per -> per.getCode()).collect(Collectors.toList());
       return permissions;
    }
    public static List<menusPermDomain> digui(List<SysPermission> permsByUserId,Long pid){
        ArrayList<menusPermDomain> list = new ArrayList<>();
        for (SysPermission p : permsByUserId) {
            if(p.getPid().equals(pid)){
                if(p.getType().intValue()!=3){
                    list.add(new menusPermDomain(p.getId(), p.getTitle(), p.getIcon(), p.getUrl(), p.getName(), digui(permsByUserId,p.getId())));
                }
            }
        }
        return list;
    }
}
