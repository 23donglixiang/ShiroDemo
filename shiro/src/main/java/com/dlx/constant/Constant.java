package com.dlx.constant;

/**
 * @author: donglixiang
 * @date: 2020/5/1 12:00
 * @description: 常量
 */
public class Constant {
    /**shiro session中用户信息-Key**/
    public static String SHIRO_SESSION_USER_INFO = "shiro_adminUserInfo";
    /**shiro session中用户所属角色-Key**/
    public static String SHIRO_SESSION_USER_ROLE = "shiro_adminUserRole";
    /**shiro session中用户所拥有角色-Key**/
    public static String SHIRO_SESSION_USER_PERMISSION = "shiro_adminUserPermission";
    /**shiro session中Subject*/
    public static String SHIRO_SESSION_USER_SUBJECT = "shiro_subject";
    /**shiro redis中session对应key的前缀**/
    public static String SHIRO_REDIS_KEY_PREFIX = "shiro_redis_session:";
    /**请求信息中的参数-用户名**/
    public static String REQUEST_PARAM_USER_NAME = "userName";
    /**请求信息中的参数-密码**/
    public static String REQUEST_PARAM_USER_PASSWORD = "passWord";
    /**请求信息中的参数-sessionId**/
    public static String REQUEST_PARAM_SESSION_ID = "sessionId";
    /**状态-成功**/
    public static String SUCCESS = "success";
    /**状态-失败**/
    public static String FAIL = "fail";
}
