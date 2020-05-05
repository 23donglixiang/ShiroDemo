package com.dlx.admin;

import lombok.Data;

/**
 * @author: donglixiang
 * @date: 2020/5/1 11:56
 * @description: 角色信息
 */
@Data
public class AdminUserRoleInfo {
    /**主键id**/
    private Integer id;
    /**角色名称**/
    private String name;
    /**角色描述**/
    private String description;
    /**创建时间**/
    private String createTime;
    /**更新时间**/
    private String updateTime;
}
