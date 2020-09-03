package com.shiro.service.impl;

import com.shiro.dao.PermissionDao;
import com.shiro.dao.PermissionDaoImpl;
import com.shiro.entity.Permission;
import com.shiro.service.PermissionService;

public class PermissionServiceImpl implements PermissionService {

    private PermissionDao permissionDao=new PermissionDaoImpl();
    public Permission createPermission(Permission permission) {
        return permissionDao.createPermission(permission);
    }

    public void deletePermission(Long permissionId) {
        permissionDao.deletePermission(permissionId);
    }
}
