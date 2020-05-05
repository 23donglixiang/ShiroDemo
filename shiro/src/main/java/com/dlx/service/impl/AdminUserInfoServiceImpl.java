package com.dlx.service.impl;

import com.dlx.admin.AdminUserInfo;
import com.dlx.dao.AdminUserInfoDao;
import com.dlx.service.AdminUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author: donglixiang
 * @date: 2020/5/1 12:07
 * @description:
 */
@Service
@Slf4j
public class AdminUserInfoServiceImpl implements AdminUserInfoService {

    @Resource
    private AdminUserInfoDao adminUserInfoDao;


    @Override
    public AdminUserInfo getAdminUserInfoByUserName(String userName){
        return adminUserInfoDao.getUserInfoByName(userName);
    }
}
