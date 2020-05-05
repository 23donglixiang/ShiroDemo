package com.dlx.realm;


import com.dlx.admin.AdminUserInfo;
import com.dlx.service.AdminRolePermissionService;
import com.dlx.service.AdminUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: donglixiang
 * @date: 2020/5/1 12:03
 * @description: 自定义realm域
 */
@Component
@Slf4j
public class UserRealm extends AuthorizingRealm {
    @Override
    public void setName(String name){
        super.setName(name);
    }

    @Autowired
    private AdminRolePermissionService adminRolePermissionService;

    @Autowired
    private AdminUserInfoService adminUserInfoService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection){
        log.info("**********授权开始**********");
        if(principalCollection==null){
            log.info("principalCollection is null");
            throw new AuthorizationException("PrincipalCollection method argument cannot be null");
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        AdminUserInfo adminUserInfo = (AdminUserInfo) principalCollection.getPrimaryPrincipal();
        if(adminUserInfo == null){
            log.info("当前用户信息为null,return");
            return null;
        }
        Integer auid = adminUserInfo.getId();
        /**赋予Subject相关角色以及权限*/
        simpleAuthorizationInfo.addRoles(adminRolePermissionService.getRoles(auid));
        simpleAuthorizationInfo.addStringPermissions(adminRolePermissionService.getPermission(auid));
        log.info("**********授权成功**********");
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken){
        log.info("**********认证开始**********");
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String userName = usernamePasswordToken.getUsername();
        char[] passWord = usernamePasswordToken.getPassword();
        AdminUserInfo adminUserInfo = adminUserInfoService.getAdminUserInfoByUserName(userName);
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(adminUserInfo,adminUserInfo.getPassword(),getName());
        log.info("**********认证结束**********");
        return simpleAuthenticationInfo;
    }

    /**
     * 清理缓存
     */
    public void clearCache() {
/*        super.getAuthorizationCache().clear();
        super.getAuthorizationCache().clear();*/
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
}
