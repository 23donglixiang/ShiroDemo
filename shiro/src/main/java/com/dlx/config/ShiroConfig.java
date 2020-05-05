package com.dlx.config;

import org.springframework.context.annotation.Configuration;
import com.dlx.listener.MySessionListener;
import com.dlx.realm.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import org.apache.shiro.mgt.SecurityManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: donglixiang
 * @date: 2020/5/1 11:59
 * @description: shiro配置类
 */
@Configuration
public class ShiroConfig {
    /**
     * shiro核心管理工具Manager：securityManager
     *
     * cacheManager：
     *              作用：用于缓存用户的授权授信，不缓存认证信息，因为授权信息量比较大，如果每次都从数据库中取的话效率可能会比较低，
     *              如果开始了CacheManager，则用户在第一次授权的时候会访问数据库，之后在进行授权都是不会从访问数据库，而是直接从缓存中拿信息，也就是说
     *              UserRealm中的doGetAuthorizationInfo方法只会执行一次，
     *              开启缓存的话就需要考虑一个问题？就是缓存更新的问题，如果用户的权限做了修改，缓存要随之进行更新。
     * */
    @Bean(name = "securityManager")
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        /**设置自定义realm*/
        securityManager.setRealm(realmCommon(hashedCredentialsMatcher()));
        /**配置自定义缓存 redis,cache默认是关闭状态*/
        securityManager.setCacheManager(cacheManager());
        /**配置自定义session管理，使用redis，代替默认的ConcurrentHashMap*/
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * 自定义Realm
     * */
    @Bean(name = "realmCommon")
    public UserRealm realmCommon(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher){
        UserRealm userRealm = new UserRealm();

        /**配置自定义密码比较器*/
        userRealm.setCredentialsMatcher(matcher);
        return new UserRealm();
    }

    /**
     * 密码加密方式
     * */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //指定加密方式为MD5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //加密次数
        credentialsMatcher.setHashIterations(1024);
        //true加密用的hex编码，false用的base64编码
        credentialsMatcher.setStoredCredentialsHexEncoded(true);

        return credentialsMatcher;
    }

    /**
     * Shiro Filter,拦截器配置
     * anon:不拦截的请求。
     * authc：必须进行认证的请求,会自动跳转到'/login'请求下，如果路径不存在就会报异常。
     * */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        /**登录成功后访问的地址，登录地址，认证失败地址*/
        shiroFilterFactoryBean.setSuccessUrl("/main");
        shiroFilterFactoryBean.setLoginUrl("/api/loginErr");
        shiroFilterFactoryBean.setUnauthorizedUrl("/error/unAuth");

        Map<String,String> filterMap = new HashMap<>();
        filterMap.put("/login","anon");
        filterMap.put("/**","authc");
        filterMap.put("/api/logoutt", "logout");

        /**取消对测试controller的拦截*/
        filterMap.put("/api/**","anon");
        filterMap.put("/test","anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterFactoryBean;
    }

    /**
     * sessionManager
     * */
    @Bean
    public SessionManager sessionManager(){
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        listeners.add(new MySessionListener());
        defaultWebSessionManager.setSessionListeners(listeners);
        /**TODO:shiro配置redis*/
        defaultWebSessionManager.setSessionDAO(sessionDAO());
        defaultWebSessionManager.setCacheManager(cacheManager());
        /**设置session有效期，单位毫秒，每次调用认证或者授权方法时会刷新session的有效期*/
        defaultWebSessionManager.setGlobalSessionTimeout(3000000);

        return defaultWebSessionManager;
    }

    /**
     * 配置SessionDao使用redis作为缓存
     * */
    @Bean
    public SessionDAO sessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * 配置CacheManager使用Redis
     * */
    @Bean
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * @description redis配置
     */
    @Bean
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("127.0.0.1");
        redisManager.setPort(6379);
        redisManager.setTimeout(10000);
        redisManager.setPassword("");
        /**设置session在redis中的过期时间，单位S，要大于等于session本身过期时间*/
        redisManager.setExpire(300);
        return redisManager;
    }

    /**
     *  开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启aop注解支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
