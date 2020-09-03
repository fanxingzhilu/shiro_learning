package com.shiro.test;

import org.junit.Assert;
import org.junit.Test;

public class IniMainTest extends BaseTest {

    @Test
    public void test(){
        login("classpath:shiro-config-main.ini","zhang","123");
        Assert.assertTrue(subject().isAuthenticated());
    }
}
