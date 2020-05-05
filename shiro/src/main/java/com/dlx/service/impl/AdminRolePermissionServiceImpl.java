package com.dlx.service.impl;

import com.dlx.dao.AdminPermissionDao;
import com.dlx.dao.AdminUserRoleDao;
import com.dlx.service.AdminRolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: donglixiang
 * @date: 2020/5/1 12:06
 * @description: serviceImpl
 */
@Service
@Slf4j
public class AdminRolePermissionServiceImpl implements AdminRolePermissionService {

    @Resource
    private AdminUserRoleDao adminUserRoleDao;

    @Resource
    private AdminPermissionDao adminPermissionDao;

    @Override
    public List<String> getRoles(Integer auId){
        List<String> roleNameList = new ArrayList<>();
        List<String> roleIdList = adminUserRoleDao.getRoleIdInfoByAuId(String.valueOf(auId));
        roleNameList = adminUserRoleDao.getRoleNameByRoleId(roleIdList);
        return roleNameList;
    }

    @Override
    public List<String> getPermission(Integer auId){
        List<String> permissionList = new ArrayList<>();
        /**
         * auId->roleId
         * roleId->resourceId
         * resourceId->permissionName
         * */
        List<String> roleIdList = adminUserRoleDao.getRoleIdInfoByAuId(String.valueOf(auId));
        permissionList = adminPermissionDao.getPermissionListByResourceId(roleIdList);

        return permissionList;
    }

}
