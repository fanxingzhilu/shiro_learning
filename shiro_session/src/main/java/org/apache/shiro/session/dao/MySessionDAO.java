package org.apache.shiro.session.dao;


import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.util.JdbcTeplateUtils;
import org.apache.shiro.util.SerializableUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;
import java.util.List;

public class MySessionDAO extends CachingSessionDAO {

    private JdbcTemplate jdbcTemplate= JdbcTeplateUtils.jdbcTemplate();
    protected void doUpdate(Session session) {
        if(session instanceof ValidatingSession && !((ValidatingSession) session).isValid()){
            return;//如果会话过期/停止 没必要再更新了
        }
        String sql = "update sessions set session=? where id=?";
        jdbcTemplate.update(sql, SerializableUtils.serialize(session), session.getId());
    }

    protected void doDelete(Session session) {
        String sql = "delete from sessions where id=?";
        jdbcTemplate.update(sql, session.getId());
    }

    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session,sessionId);
        String sql="insert into sessions(id,session) values(?,?)";
        jdbcTemplate.update(sql,sessionId,SerializableUtils.serialize(session));
        return null;
    }

    protected Session doReadSession(Serializable sessionId) {
        String sql = "select session from sessions where id=?";
        List<String> sessionStrList = jdbcTemplate.queryForList(sql, String.class, sessionId);
        if(sessionStrList.size() == 0) return null;
        return SerializableUtils.deserialize(sessionStrList.get(0));
    }
}
