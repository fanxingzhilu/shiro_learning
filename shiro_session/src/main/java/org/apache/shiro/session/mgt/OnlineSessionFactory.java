package org.apache.shiro.session.mgt;

import org.apache.shiro.session.Session;
import org.apache.shiro.util.IpUtils;
import org.apache.shiro.web.session.mgt.WebSessionContext;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * 创建自定义的session，
 * 添加一些自定义的数据
 * 如 用户登录到的系统ip
 * 用户状态（在线 隐身 强制退出）
 * 等 比如当前所在系统等
 */
public class OnlineSessionFactory implements SessionFactory {
    public Session createSession(SessionContext sessionContext) {
        OnlineSession session = new OnlineSession();
        if(sessionContext!=null&& sessionContext instanceof WebSessionContext){
            WebSessionContext webSessionContext = (WebSessionContext) sessionContext;
            HttpServletRequest request = (HttpServletRequest) webSessionContext.getServletRequest();
            if(request!=null){
                session.setHost(IpUtils.getIpAddr(request));
                session.setUserAgent(request.getHeader("User-Agent"));
                session.setSystemHost(request.getLocalAddr() + ":" + request.getLocalPort());
            }
        }
        return session;
    }
}
