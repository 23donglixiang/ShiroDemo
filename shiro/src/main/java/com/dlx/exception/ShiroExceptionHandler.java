package com.dlx.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * @author: donglixiang
 * @date: 2020/5/1 12:01
 * @description: shiro异常信息捕捉
 */
@ControllerAdvice
@Slf4j
public class ShiroExceptionHandler {
    /**
     * @description 用户未登陆异常
     */
    @ExceptionHandler({AuthorizationException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String AuthorizationException(final NativeWebRequest request, final AuthorizationException e){
        log.error("当前用户未登陆,error{}",e);
        return "Please Login First , Then Visit";
    }


    /**
     * @description 权限不足异常
     */
    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String UnAuthorizationException(final NativeWebRequest request,final UnauthorizedException e){
        log.error("当前用户权限不足错误,error{}",e);
        return "You Have Not Enough Permission To Visit! Please Return ";
    }
}
