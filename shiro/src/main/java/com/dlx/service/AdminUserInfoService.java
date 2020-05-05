package com.dlx.service;


import com.dlx.admin.AdminUserInfo;

/**
 * @author: donglixiang
 * @date: 2020/5/1 12:06
 * @description: 用户信息service
 */
public interface AdminUserInfoService {

    /**
     * @description 根据用户名查询用户信息
     * @param userName 用户名
     * @return 用户信息实体
     */
    AdminUserInfo getAdminUserInfoByUserName(String userName);
}
