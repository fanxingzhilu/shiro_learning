package com.shiro.service;

import com.shiro.entity.User;

import java.util.Set;

public interface UserService {

    /**
     * 创建用户
     * @param user
     */
    public void createUser(User user);

    /**
     * 修改密码
     * @param userId
     * @param newPassword
     */

    public void changePassword(Long userId, String newPassword);

    /**
     * 添加用户-角色关系
     * @param userId
     * @param roleIds
     */
    public void correlationRoles(Long userId, Long... roleIds);

    /**
     * 移除用户-角色关系
     * @param userId
     * @param roleIds
     */
    public void uncorrelationRoles(Long userId, Long... roleIds);

    /**
     * 根据用户名查用户
     * @param username
     * @return
     */
    public User findByUsername(String username);

    /**
     * 根据用户名查其角色
     * @param username
     * @return
     */
    public Set<String> findRoles(String username);

    /**
     * 根据用户名查其权限
     * @param username
     * @return
     */
    public Set<String> findPermissions(String username);
}
