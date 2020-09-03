package com.shiro.test;

import org.junit.Assert;
import org.junit.Test;

public class AuthorizerTest extends BaseTest {

    @Test
    public void testIsPermitted(){
        login("classpath:shiro-authorizer.ini","zhang","123");
        //判断拥有权限：user:create
        Assert.assertTrue(subject().isPermitted("user1:update"));
        Assert.assertTrue(subject().isPermitted("user2:update"));
        Assert.assertTrue(subject().isPermitted("+user1+2"));//修改权限
        Assert.assertTrue(subject().isPermitted("+user1+8"));//查看权限
        Assert.assertTrue(subject().isPermitted("+user1+10"));//修改及查看权限

        Assert.assertFalse(subject().isPermitted("+user1+4"));//没有删除权限

        Assert.assertTrue(subject().isPermitted("menu:view"));//通过MyRolePermissionResolver解析得到的权限
    }
}
