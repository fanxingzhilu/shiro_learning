package org.apache.shiro.session.scheduler;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.*;
import org.apache.shiro.util.JdbcTeplateUtils;
import org.apache.shiro.util.SerializableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class MySessionValidationScheduler implements SessionValidationScheduler,Runnable {

    private JdbcTemplate jdbcTemplate= JdbcTeplateUtils.jdbcTemplate();
    private static final Logger log= LoggerFactory.getLogger(MySessionValidationScheduler.class);
    ValidatingSessionManager sessionManager;
    private ScheduledExecutorService service;
    private long interval= DefaultSessionManager.DEFAULT_SESSION_VALIDATION_INTERVAL;
    private boolean enabled=false;

    public MySessionValidationScheduler() {
        super();
    }

    public ValidatingSessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(ValidatingSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }


    public boolean isEnabled() {
        return this.enabled;
    }

    public void enableSessionValidation() {
        if (this.interval > 0L) {
            this.service = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    return thread;
                }
            });
            this.service.scheduleAtFixedRate(this, this.interval, this.interval, TimeUnit.MILLISECONDS);
            this.enabled = true;
        }
    }

    public void disableSessionValidation() {
        this.service.shutdownNow();
        this.enabled = false;
    }
    public void run() {
        if(log.isDebugEnabled()){
            log.debug("Executing session validation...");
        }
        long startTime=System.currentTimeMillis();
        //分页获取会话并验证
        String sql="select session from sessions limit ?,?";
        int start=0;
        int size=20;
        List<String> sessionList = jdbcTemplate.queryForList(sql, String.class, start, size);
        while (sessionList.size()>0){
            try {
                for(String sessionStr : sessionList){
                    Session session = SerializableUtils.deserialize(sessionStr);
                    Method validate = ReflectionUtils.findMethod(AbstractValidatingSessionManager.class, "validate", Session.class, SessionKey.class);
                    validate.setAccessible(true);
                    ReflectionUtils.invokeMethod(validate,sessionManager,session,new DefaultSessionKey(session.getId()));
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            start=start+size;
            sessionList=jdbcTemplate.queryForList(sql,String.class,start,size);
        }
        long stopTime = System.currentTimeMillis();
        if(log.isDebugEnabled()){
            log.debug("Session validation completed successfully in " + (stopTime - startTime) + " milliseconds.");
        }
    }
}
