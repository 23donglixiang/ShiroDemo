package com.dlx.service;

import java.util.List;

/**
 * @author: donglixiang
 * @date: 2020/5/1 12:06
 * @description: 操作用户信息、角色service
 */
public interface AdminRolePermissionService {

    /**
     * @description 获取角色列表
     * @param auid 用户id
     * @return 角色列表
     */
    List<String> getRoles(Integer auid);

    /**
     * @description 获取权限列表
     * @param auid 用户id
     * @return 权限列表
     */
    List<String> getPermission(Integer auid);

}
