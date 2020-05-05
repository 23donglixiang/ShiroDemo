package com.dlx.dao;

import com.dlx.admin.AdminUserInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author: donglixiang
 * @date: 2020/5/1 12:09
 * @description: 用户信息Dao
 */
@Repository
public interface AdminUserInfoDao {

    /**
     * @description 通过userName查询用户信息实体
     * @param userName 用户名
     * @return 用户信息实体
     */
    @Select("select * from shiro_user where userName='${userName}'")
    AdminUserInfo getUserInfoByName(@Param("userName") String userName);
}
