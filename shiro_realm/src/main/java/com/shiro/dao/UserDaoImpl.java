package com.shiro.dao;

import com.shiro.entity.User;
import com.shiro.util.JdbcTemplateUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate jdbcTemplate= JdbcTemplateUtil.jdbcTemplate();
    public User createUser( final User user) {
        final String sql="insert into sys_users(username,password,salt,locked) values(?,?,?,?)";
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1,user.getUsername());
                ps.setString(2,user.getPassword());
                ps.setString(3,user.getSalt());
                ps.setBoolean(4,user.getLocked());
                return ps;
            }
        },generatedKeyHolder);
        user.setId(generatedKeyHolder.getKey().longValue());
        return user;
    }

    public void updateUser(User user) {
        String sql="update sys_users set username=?,password=?,salt=?,locked=? where id=?";
        jdbcTemplate.update(sql,user.getUsername(),user.getPassword(),user.getSalt(),user.getLocked(),user.getId());
    }

    public void deleteUser(Long userId) {
        String sql="delete from sys_users where id=?";
        jdbcTemplate.update(sql,userId);
    }

    public void correlationRoles(Long userId, Long... roleIds) {
        if(roleIds==null||roleIds.length==0){
            return;
        }
        String sql="insert into sys_users_roles values(?,?)";
        for(Long roleId :roleIds){
            if(!exists(userId,roleId)){
                jdbcTemplate.update(sql,userId,roleId);
            }
        }
    }

    public void uncorrelationRoles(Long userId, Long... roleIds) {
        if(roleIds==null||roleIds.length==0){
            return;
        }
        String sql="delete from sys_users_roles where user_id=? and role_id=?";
        for(Long roleId:roleIds){
            if(exists(userId,roleId)){
                jdbcTemplate.update(sql,userId,roleId);
            }
        }
    }

    private boolean exists(Long userId,Long roleId){
        String sql="Select count(1) from sys_users_roles where user_id=? and role_id=?";
        return jdbcTemplate.queryForObject(sql,Integer.class,userId,roleId)!=0;
    }
    public User findOne(Long userId) {
        String sql="select * from sys_users where id=?";
        List<User> userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class), userId);
        if(userList==null||userList.size()==0){
            return null;
        }
        return userList.get(0);
    }

    public User findByUsername(String username) {
        String sql="select * from sys_users where username=?";
        List<User> userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class), username);
        if(userList==null||userList.size()==0){
            return null;
        }
        return userList.get(0);
    }

    public Set<String> findRoles(String username) {
        String sql="select role from sys_users u,sys_roles r,sys_users_roles ur where u.username=? and u.id=ur.user_id and r.id=ur.role_id";
        return new HashSet<String>(jdbcTemplate.queryForList(sql,String.class,username));
    }

    public Set<String> findPermissions(String username) {
        String sql="select permission from sys_users u, sys_roles r, sys_permissions p, sys_users_roles ur, sys_roles_permissions rp where u.username=? and u.id=ur.user_id and r.id=ur.role_id and r.id=rp.role_id and p.id=rp.permission_id";
        return new HashSet<String>(jdbcTemplate.queryForList(sql,String.class,username));
    }
}
