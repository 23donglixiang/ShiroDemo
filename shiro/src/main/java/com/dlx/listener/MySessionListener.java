package com.dlx.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: donglixiang
 * @date: 2020/5/1 12:03
 * @description: Session监听类
 */
public class MySessionListener implements SessionListener {
    private final AtomicInteger sessionCount = new AtomicInteger(0);

    @Override
    public void onStart(Session session) {
        sessionCount.incrementAndGet();
        //System.out.println("登录+1=="+sessionCount.get());
    }

    @Override
    public void onStop(Session session) {
        sessionCount.decrementAndGet();
        //System.out.println("登录退出-1=="+sessionCount.get());
    }

    @Override
    public void onExpiration(Session session) {
        sessionCount.decrementAndGet();
        //System.out.println("登录过期-1=="+sessionCount.get());
    }

    public int getSessionCount() {
        return sessionCount.get();
    }

}
