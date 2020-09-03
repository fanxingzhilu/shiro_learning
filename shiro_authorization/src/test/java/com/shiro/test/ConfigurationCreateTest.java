package com.shiro.test;

import org.junit.Assert;
import org.junit.Test;

public class ConfigurationCreateTest extends BaseTest {

    @Test
    public void test(){
        login("classpath:shiro-config.ini","zhang","123");
        Assert.assertTrue(subject().isAuthenticated());
    }
}
