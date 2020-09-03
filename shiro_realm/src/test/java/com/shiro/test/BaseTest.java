package com.shiro.test;

import com.shiro.entity.Permission;
import com.shiro.entity.Role;
import com.shiro.entity.User;
import com.shiro.service.PermissionService;
import com.shiro.service.RoleService;
import com.shiro.service.UserService;
import com.shiro.service.impl.PermissionServiceImpl;
import com.shiro.service.impl.RoleServiceImpl;
import com.shiro.service.impl.UserServiceImpl;
import com.shiro.util.JdbcTemplateUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Before;

public abstract class BaseTest {
    protected PermissionService permissionService=new PermissionServiceImpl();
    protected RoleService roleService=new RoleServiceImpl();
    protected UserService userService=new UserServiceImpl();

    protected String password = "123";

    protected Permission p1;
    protected Permission p2;
    protected Permission p3;
    protected Role r1;
    protected Role r2;
    protected User u1;
    protected User u2;
    protected User u3;
    protected User u4;
    @Before
    public void setUp(){
        //先删除
        JdbcTemplateUtil.jdbcTemplate().update("delete from sys_users");
        JdbcTemplateUtil.jdbcTemplate().update("delete from sys_roles");
        JdbcTemplateUtil.jdbcTemplate().update("delete from sys_permissions");
        JdbcTemplateUtil.jdbcTemplate().update("delete from sys_users_roles");
        JdbcTemplateUtil.jdbcTemplate().update("delete from sys_roles_permissions");
        //新增权限
        p1=new Permission("user:create","用户模块新增",Boolean.TRUE);
        p2=new Permission("user:update","用户模块修改",Boolean.TRUE);
        p3=new Permission("user:create","用户模块新增",Boolean.TRUE);
        p1=new Permission("menu:create","菜单模块新增",Boolean.TRUE);
        permissionService.createPermission(p1);
        permissionService.createPermission(p2);
        permissionService.createPermission(p3);

        //2、新增角色
        r1 = new Role("admin", "管理员", Boolean.TRUE);
        r2 = new Role("user", "用户管理员", Boolean.TRUE);
        roleService.createRole(r1);
        roleService.createRole(r2);

        //3、关联角色-权限
        roleService.correlationPermissions(r1.getId(), p1.getId());
        roleService.correlationPermissions(r1.getId(), p2.getId());
        roleService.correlationPermissions(r1.getId(), p3.getId());

        roleService.correlationPermissions(r2.getId(), p1.getId());
        roleService.correlationPermissions(r2.getId(), p2.getId());

        //新增用户
        u1 = new User("zhang", password);
        u2 = new User("li", password);
        u3 = new User("wu", password);
        u4 = new User("wang", password);
        u4.setLocked(Boolean.TRUE);
        userService.createUser(u1);
        userService.createUser(u2);
        userService.createUser(u3);
        userService.createUser(u4);
        //5、关联用户-角色
        userService.correlationRoles(u1.getId(), r1.getId());
    }

    @After
    public void tearDown(){
        ThreadContext.unbindSubject();
    }

    protected void login(String configFile,String username,String password){
        IniSecurityManagerFactory securityManagerFactory = new IniSecurityManagerFactory(configFile);
        SecurityManager securityManager = securityManagerFactory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        subject.login(token);
    }
    public Subject subject(){
        return SecurityUtils.getSubject();
    }
}
