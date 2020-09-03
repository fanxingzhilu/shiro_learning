package com.shiro.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.util.Arrays;
import java.util.Collection;

/**
 * 用于根据角色字符串来解析得到权限集
 */
public class MyRolePermissionResolver implements RolePermissionResolver {
    //此处的实现很简单，如果用户拥有 role1，那么就返回一个“menu:*”的权限。
    public Collection<Permission> resolvePermissionsInRole(String s) {
        if("role1".equals(s)){
            return Arrays.asList((Permission) new WildcardPermission("menu:*"));
        }
        return null;
    }
}
