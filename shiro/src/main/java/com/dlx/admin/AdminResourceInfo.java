package com.dlx.admin;

import lombok.Data;

/**
 * @author: donglixiang
 * @date: 2020/5/1 11:54
 * @description: 菜单信息
 */
@Data
public class AdminResourceInfo {
    /**id**/
    private Integer id;
    /**父级目录id**/
    private String parentId;
    /**资源名称**/
    private String name;
    /**创建时间**/
    private String createTime;
    /**修改时间**/
    private String updateTime;
}
