package com.shiro.service.impl;

import com.shiro.dao.RoleDao;
import com.shiro.dao.RoleDaoImpl;
import com.shiro.entity.Role;
import com.shiro.service.RoleService;

public class RoleServiceImpl implements RoleService {
    private RoleDao roleDao=new RoleDaoImpl();
    public Role createRole(Role role) {
        return roleDao.createRole(role);
    }

    public void deleteRole(Long roleId) {
        roleDao.deleteRole(roleId);
    }

    public void correlationPermissions(Long roleId, Long... permissionIds) {
        roleDao.correlationPermissions(roleId,permissionIds);
    }

    public void uncorrelationPermissions(Long roleId, Long... permissionIds) {
        roleDao.uncorrelationPermissions(roleId,permissionIds);
    }
}
