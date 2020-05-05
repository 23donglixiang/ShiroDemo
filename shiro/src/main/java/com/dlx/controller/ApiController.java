package com.dlx.controller;

import com.dlx.admin.AdminUserInfo;
import com.dlx.constant.Constant;
import com.dlx.realm.UserRealm;
import com.dlx.service.AdminRolePermissionService;
import com.dlx.service.impl.AdminUserInfoServiceImpl;
import com.dlx.util.redis.RedisUtil;
import com.dlx.util.redis.SerializerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * @author donglixiang
 * @date 2020/5/1 10:09
 * @description ApiController
 */
@Controller
@RequestMapping("/api")
@Slf4j
public class ApiController {
    @Autowired
    private AdminUserInfoServiceImpl adminUserInfoService;

    @Autowired
    private AdminRolePermissionService adminRolePermissionService;

    @Value("${name.usr}")
    private String name;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private UserRealm userRealm;


    /**
     * @description 测试配置文件
     */
    @RequestMapping(value = "/test",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String testApi(HttpServletRequest request, HttpServletResponse response){
        System.out.println(name);
        return "success";
    }

    /**
     * @description 测试数据库连接
     */
    @RequestMapping(value = "/mysql",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String testMysql(HttpServletRequest request){
        AdminUserInfo adminUserInfo = adminUserInfoService.getAdminUserInfoByUserName(request.getParameter("userName"));
        List<String> roleList = adminRolePermissionService.getRoles(adminUserInfo.getId());
        List<String> permissionList = adminRolePermissionService.getPermission(adminUserInfo.getId());
        return adminUserInfo.toString();
    }

    @RequestMapping(value = "/loginErr",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String loginErr(){
        log.info("请先登录");
        return "请先登录";
    }
    /**
     * @description 模拟用户登录
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response){
        String userName = request.getParameter(Constant.REQUEST_PARAM_USER_NAME);
        String passWord = request.getParameter(Constant.REQUEST_PARAM_USER_PASSWORD);
        log.info("登录请求,参数为：用户名【{}】,密码【{}】",userName,passWord);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userName,passWord);
        /**是否开启rememberMe功能，true：启用，false：禁用*/
        usernamePasswordToken.setRememberMe(false);
        String sessionId = "";
        try {
            SecurityUtils.getSubject().login(usernamePasswordToken);
            Subject subject = SecurityUtils.getSubject();
            Session session = SecurityUtils.getSubject().getSession();
            sessionId = (String) session.getId();
            AdminUserInfo adminUserInfo = adminUserInfoService.getAdminUserInfoByUserName(userName);
            session.setAttribute(Constant.SHIRO_SESSION_USER_INFO,adminUserInfo);
            session.setAttribute(Constant.SHIRO_SESSION_USER_ROLE,adminRolePermissionService.getRoles(adminUserInfo.getId()));
            session.setAttribute(Constant.SHIRO_SESSION_USER_PERMISSION,adminRolePermissionService.getPermission(adminUserInfo.getId()));
            /**sessionId存到redis中*/
            StringBuilder stringBuilder = new StringBuilder("user_").append(adminUserInfo.getId()).append("_").append(adminUserInfo.getUserName());
            boolean isSuccess = redisUtil.setString(stringBuilder.toString(),(Constant.SHIRO_REDIS_KEY_PREFIX+sessionId),0);
            if(!isSuccess){
                return "redis set操作失败";
            }
            log.info("用户【{}】登录成功，sessionId为 【{}】",userName,sessionId);
        } catch (UnknownAccountException e){
            e.printStackTrace();
            log.info("用户{}不存在！",userName);
            return "用户不存在";
        } catch (IncorrectCredentialsException e){
            e.printStackTrace();
            log.info("用户{}密码不正确",userName);
            return "密码不正确";
        } catch (AuthenticationException e){
            e.printStackTrace();
            log.info("账号{}未注册",userName);
            return "账号未注册";
        } catch (Exception e){
            e.printStackTrace();
            log.info("异常");
            return "Exception";
        }

        return sessionId;
    }

    /**
     * @description 根据SessionId获取Subject
     * 同一个项目中可以使用这种方式
     */
    @RequestMapping(value = "/getSubject",method = RequestMethod.POST)
    @ResponseBody
    public String getSubjectFromSession(HttpServletRequest request,HttpServletResponse response){
        String sessionId = request.getParameter(Constant.REQUEST_PARAM_SESSION_ID);
        log.info("入参sessionId为【{}】",sessionId);
        SessionKey sessionKey = new SessionKey() {
            @Override
            public Serializable getSessionId() {
                return sessionId;
            }
        };
        Session session = null;
        try{
            session = SecurityUtils.getSecurityManager().getSession(sessionKey);
        } catch (UnknownSessionException e){
            log.info("Id为{}， 对应的session不存在",sessionId);
            return "Id为"+sessionId+"，对应的session不存在";
        }
        /**session存在并不能确定用户已经登录成功，因为shiro启动时候会默认创建一个Subject和Session。
         * 登录时会将用户对象set到session中，可以加一步判断用户对象是否为null来判断用户登录成功
         * */
        AdminUserInfo adminUserInfo = (AdminUserInfo) session.getAttribute(Constant.SHIRO_SESSION_USER_INFO);
        if(adminUserInfo == null){
            log.info("用户未登录或登录失败，请重新登录");
            return "用户未登录或登录失败，请重新登录";
        }
        log.info(adminUserInfo.toString());
        return (String) session.getId();
    }

    /**
     * @description Subject登出,清楚关于Subject的全部信息
     */
    @RequestMapping(value = "/logout",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String logout(){
        SecurityUtils.getSubject().logout();
        log.info("账号登出");
        return "登出成功";
    }

    @RequestMapping(value = "/testRole",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    //@RequiresRoles("administrator")
    @RequiresPermissions("权限5")
    @ResponseBody
    public String testRole(){
        return Constant.SUCCESS;
    }

    /**
     * @description 根据sessionId获取redis中的key
     */
    @RequestMapping(value = "/getRedisKey",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getRedisKey(HttpServletRequest request){
        String sessionId = request.getParameter(Constant.REQUEST_PARAM_SESSION_ID);
        String key = Constant.SHIRO_REDIS_KEY_PREFIX+sessionId;
        return key.replace(" ","");
    }

    @RequestMapping("/logoutt")
    public void logoutt(){
        log.info("退出成功-------");
    }

    /**
     * @description 清理缓存
     */
    @RequestMapping(value = "/clearCacheManager",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String clearCacheManager(){
        try {
            SecurityUtils.getSubject().logout();
        } catch (IllegalArgumentException e){
            log.info("当前Subject为null，logout失败");
            return Constant.FAIL;
        } catch (Exception e){
            log.info("logout失败");
            return Constant.FAIL;
        }

        return Constant.SUCCESS;
    }
}
