package com.shiro.dao;

import com.shiro.entity.Permission;
import com.shiro.util.JdbcTemplateUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PermissionDaoImpl implements PermissionDao {
    private final JdbcTemplate jdbcTemplate= JdbcTemplateUtil.jdbcTemplate();

    public Permission createPermission( final Permission permission) {
       final String sql="insert into sys_permissions(permission,description,available) values(?,?,?)";
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, permission.getPermission());
                ps.setString(2, permission.getDescription());
                ps.setBoolean(3, permission.getAvailable());
                return ps;
            }
        },generatedKeyHolder);
        permission.setId(generatedKeyHolder.getKey().longValue());
        return  permission;
    }

    public void deletePermission(Long permissionId) {
        //首先把与permission关联的相关表的数据删掉
        String sql = "delete from sys_roles_permissions where permission_id=?";
        jdbcTemplate.update(sql, permissionId);

        sql = "delete from sys_permissions where id=?";
        jdbcTemplate.update(sql, permissionId);
    }
}
