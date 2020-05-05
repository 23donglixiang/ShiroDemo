package com.dlx.admin;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: donglixiang
 * @date: 2020/5/1 11:56
 * @description: 用户信息
 */
@Data
public class AdminUserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id**/
    private Integer id;
    /**登录用户名**/
    private String userName;
    /**登录密码**/
    private String password;
    /**创建时间**/
    private String createTime;
    /**修改时间**/
    private String updateTime;
}
