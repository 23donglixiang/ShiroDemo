package com.dlx.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: donglixiang
 * @date: 2020/5/1 12:09
 * @description:
 */
@Repository
public interface AdminUserRoleDao {

    /**
     * @description 通过用户id获取所属角色id
     * @param auid 用户id
     * @return 角色集合
     */
    @Select("select role_id from shiro_user_role where auid='${auid}'")
    List<String> getRoleIdInfoByAuId(@Param("auid") String auid);

    /**
     * @description 根据角色id获取角色名称
     * @param roleIdList
     * @return roleNameList
     */
    @Select("<script>" +
            "select name from shiro_role where id in(" +
            "<foreach collection='roleIdList' separator=',' item='roleId'>#{roleId}</foreach>)" +
            "</script>")
    List<String> getRoleNameByRoleId(@Param("roleIdList") List<String> roleIdList);
}
