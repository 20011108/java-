package com.hfut.stock.service.impl;

import com.hfut.stock.exception.BusinessException;
import com.hfut.stock.mapper.SysPermissionMapper;
import com.hfut.stock.mapper.SysRolePermissionMapper;
import com.hfut.stock.pojo.domin.SysPermissionDomain;
import com.hfut.stock.pojo.entity.SysPermission;
import com.hfut.stock.service.PermsService;
import com.hfut.stock.utils.IdWorker;
import com.hfut.stock.vo.req.PermissionAddVo;
import com.hfut.stock.vo.req.PermissionUpdateVo;
import com.hfut.stock.vo.resp.R;
import com.hfut.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Author:yuyang
 * Date:2024-05-02
 * Time:16:12
 */
@Service
@Slf4j
public class PermsServiceImpl implements PermsService {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Override
    public R<List<SysPermission>> getAllPerms() {
        List<SysPermission> permissionList = sysPermissionMapper.selectAll();
        if (CollectionUtils.isEmpty(permissionList)){
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(permissionList);
    }

    @Override
    public R<List<SysPermissionDomain>> getPermsTree() {
       /*List<SysPermission> list1= sysPermissionMapper.selectByType(0001);
        if (CollectionUtils.isEmpty(list1)){
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
       List<SysPermission> list2= sysPermissionMapper.selectByType(0002);
        if (CollectionUtils.isEmpty(list2)){
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }*/
        List<SysPermissionDomain> permsTreeList = new ArrayList<>();
        List<SysPermission> all = sysPermissionMapper.selectAll();
        permsTreeList.add(SysPermissionDomain.builder().id(0L).title("顶级菜单").level(0).build());
        permsTreeList.addAll(getPermissionLevelTree(all,0l,1));
        /*for (SysPermission sysPermission : list1) {
            permsTreeList.add(new SysPermissionDomain(sysPermission));
            for (SysPermission permission : list2) {
                if(permission.getPid().equals(sysPermission.getId())){
                    permsTreeList.add(new SysPermissionDomain(permission));
                }
            }
        }*/
        return R.ok(permsTreeList);
    }
    /**
     * 递归设置级别，用于权限列表 添加/编辑 所属菜单树结构数据
     * @param permissions 权限集合
     * @param parentId 父级id
     * @param lavel 级别
     * @return
     */
    private List<SysPermissionDomain> getPermissionLevelTree(List<SysPermission> permissions, Long parentId, int lavel) {
        List<SysPermissionDomain> result=new ArrayList<>();
        for (SysPermission permission : permissions) {
            if (permission.getType().intValue()!=3 && permission.getPid().equals(parentId)) {
                SysPermissionDomain nodeTreeVo = new SysPermissionDomain();
                nodeTreeVo.setId(permission.getId());
                nodeTreeVo.setTitle(permission.getTitle());
                nodeTreeVo.setLevel(lavel);
                result.add(nodeTreeVo);
                result.addAll(getPermissionLevelTree(permissions,permission.getId(),lavel+1));
            }
        }
        return result;
    }

    @Override
    public R addPerms(PermissionAddVo addVo) {
        SysPermission addPerms = new SysPermission();
        BeanUtils.copyProperties(addVo,addPerms);
        checkPermissionForm(addPerms);
        addPerms.setId(idWorker.nextId());
        int i=sysPermissionMapper.addPerms(addPerms);
        if(i>0){
            return R.ok("添加成功");
        }
        else return R.error(ResponseCode.DATA_ERROR.getMessage());
    }

    @Override
    public R updatePerms(PermissionUpdateVo updateVo) {
        SysPermission addPerms = new SysPermission();
        BeanUtils.copyProperties(updateVo,addPerms);
        checkPermissionForm(addPerms);
        int i=sysPermissionMapper.updatePerms(addPerms);
        if(i>0){
            return R.ok("更新成功");
        }
        else return R.error(ResponseCode.DATA_ERROR.getMessage());
    }

    @Override
    public R deletePerms(Long permissionId) {
        /*int i=sysPermissionMapper.deletePerms(permissionId);
        if(i>0){
            return R.ok("删除成功");
        }
        else return R.error(ResponseCode.DATA_ERROR.getMessage());*/
        //1.判断当前角色是否有角色自己，有则不能删除
        int count =this.sysPermissionMapper.findChildrenCountByParentId(permissionId);
        if (count>0) {
            throw new BusinessException(ResponseCode.ROLE_PERMISSION_RELATION.getMessage());
        }
        //2.删除角色关联权限的信息
        this.sysRolePermissionMapper.deleteByPermissionId(permissionId);
        //3.更新权限状态为已删除
        SysPermission permission = SysPermission.builder().id(permissionId).deleted(0).updateTime(new Date()).build();
        int updateCount = this.sysPermissionMapper.updateByPrimaryKeySelective(permission);
        if (updateCount!=1) {
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }
    /**
     * 检查添加或者更新的权限提交表单是否合法，如果不合法，则直接抛出异常
     * 检查规则：目录的父目录等级必须为0或者其他目录（等级为1）
     *          菜单的父父级必须是1，也就是必须是父目录，
     *          按钮的父级必须是菜单，也是是等级是3，且父级是2
     *          其他关联的辨识 url等信息也可做相关检查
     * @param vo
     */
    private void checkPermissionForm(SysPermission vo) {
        if (vo!=null || vo.getType()!=null || vo.getPid()!=null){
            //获取权限类型 0：顶级目录 1.普通目录 2.菜单 3.按钮
            Integer type = vo.getType();
            //获取父级id
            Long pid = vo.getPid();
            //根据父级id查询父级信息
            SysPermission parentPermission = this.sysPermissionMapper.selectByPrimaryKey(pid);
            if (type==1){
                if(!pid.equals("0") || (parentPermission!=null && parentPermission.getType()> 1)){
                    throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR.getMessage());
                }
            }
            else if (type==2){
                if (parentPermission==null || parentPermission.getType() !=1 ){
                    throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR.getMessage());
                }
                if (StringUtils.isBlank(vo.getUrl())){
                    throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_URL_CODE_NULL.getMessage());
                }
            }
            else if (type==3){
                if (parentPermission==null || parentPermission.getType()!=2){
                    throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_BTN_ERROR.getMessage());
                }
                else if (vo.getUrl()==null || vo.getCode()==null || vo.getMethod()==null){
                    throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
                }
            }
            else {
                throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
            }
        }else {
            throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
        }
    }
}
