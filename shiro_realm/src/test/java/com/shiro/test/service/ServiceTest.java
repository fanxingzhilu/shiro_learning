package com.shiro.test.service;

import com.shiro.test.BaseTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class ServiceTest extends BaseTest {

    @Test
    public void testUserRolePermissionRelation(){
        Set<String> roles = userService.findRoles(u1.getUsername());
        System.out.println(roles.size());
        System.out.println(roles.contains(r1.getRole()));
        Set<String> permissions = userService.findPermissions(u1.getUsername());
        System.out.println(permissions.size());
        System.out.println(permissions.contains(p3.getPermission()));

        //li
        roles = userService.findRoles(u2.getUsername());
        System.out.println(roles.size());
        permissions = userService.findPermissions(u2.getUsername());
        System.out.println(permissions.size());

        //解除 admin-menu:update关联
        roleService.uncorrelationPermissions(r1.getId(), p3.getId());
        permissions = userService.findPermissions(u1.getUsername());
        Assert.assertEquals(2, permissions.size());
        Assert.assertFalse(permissions.contains(p3.getPermission()));


        //删除一个permission
        permissionService.deletePermission(p2.getId());
        permissions = userService.findPermissions(u1.getUsername());
        Assert.assertEquals(1, permissions.size());

        //解除 zhang-admin关联
        userService.uncorrelationRoles(u1.getId(), r1.getId());
        roles = userService.findRoles(u1.getUsername());
        Assert.assertEquals(0, roles.size());
    }
}
