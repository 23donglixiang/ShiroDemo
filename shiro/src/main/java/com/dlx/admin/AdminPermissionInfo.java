package com.dlx.admin;

import lombok.Data;

/**
 * @author: donglixiang
 * @date: 2020/5/1 11:54
 * @description: 权限信息
 */
@Data
public class AdminPermissionInfo {
    /**id**/
    private Integer id;
    /**资源id**/
    private String resourceId;
    /**权限名称**/
    private String permissionName;
    /**创建时间**/
    private String createTime;
    /**更新时间**/
    private String updteTime;
}
