package com.dlx.controller;

import com.dlx.admin.AdminUserInfo;
import com.dlx.constant.Constant;
import com.dlx.service.AdminUserInfoService;
import com.dlx.util.redis.RedisUtil;
import com.dlx.util.redis.SerializerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: donglixiang
 * @date: 2020/5/1 12:47
 * @description: Shiro Test
 */
@Controller
@RequestMapping("/shirotest")
@Slf4j
public class ShiroTestController {
    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private AdminUserInfoService adminUserInfoService;

    /**
     * @description 获取session
     */
    @RequestMapping(value = "/getSession",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getSession(HttpServletRequest request){

        String userName = request.getParameter(Constant.REQUEST_PARAM_USER_NAME);
        AdminUserInfo adminUserInfo = adminUserInfoService.getAdminUserInfoByUserName(userName);
        String key = new StringBuilder("user_").append(adminUserInfo.getId()).append("_").append(adminUserInfo.getUserName()).toString();
        String sessionId = redisUtil.getString(key);
        if(!redisUtil.exist(sessionId.getBytes())){
            return "此sessionId对应session不存在";
        }
        Session session = (Session) SerializerUtil.deserialize(redisUtil.getObject(sessionId.getBytes()));
        List<String> roleList = (List<String>) session.getAttribute(Constant.SHIRO_SESSION_USER_ROLE);
        for(String tole:roleList){
            /**TODO:判断是否拥有某个角色*/
        }
        List<String> permissionList = (List<String>) session.getAttribute(Constant.SHIRO_SESSION_USER_PERMISSION);
        for(String permission:permissionList){
            /**TODO：判断是否拥有某个权限*/
        }

        Subject subject = (Subject) session.getAttribute(Constant.SHIRO_SESSION_USER_SUBJECT);
        /**TODO:如果修改了用户的权限，需要调用logout来清除过期的缓存*/
        return adminUserInfo.toString();
    }
}
