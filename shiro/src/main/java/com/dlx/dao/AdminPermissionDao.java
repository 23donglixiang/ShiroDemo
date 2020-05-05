package com.dlx.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: donglixiang
 * @date: 2020/5/1 12:08
 * @description: 权限信息Dao
 */
@Repository
public interface AdminPermissionDao {

    /**
     * @description 根据resourceId获取权限信息
     * @param roleIdList
     * @return permissionList
     */
    @Select("<script>" +
            "select permission_name from shiro_permission where role_id in(" +
            "<foreach collection='roleIdList' separator=',' item='roleId'>#{roleId}</foreach>)" +
            "</script>")
    List<String> getPermissionListByResourceId(@Param("roleIdList") List<String> roleIdList);
}
