package com.shiro.hash;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;

public abstract class BaseTest {
    @After
    public void tearDown(){
        ThreadContext.unbindSubject();
    }

    protected void login(String configFile,String username,String password){
        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        IniSecurityManagerFactory securityManagerFactory = new IniSecurityManagerFactory(configFile);
        //2、得到SecurityManager实例 并绑定给SecurityUtils
        SecurityManager securityManager = securityManagerFactory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            System.out.println("登录成功");
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

    }

    public Subject subject(){
        return SecurityUtils.getSubject();
    }
}
