package org.apache.shiro.session.filter;

import org.apache.shiro.ShiroConstants;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.OnlineSession;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class OnlineSessionFilter extends AccessControlFilter {
    private String forceLogoutUrl;

    private SessionDAO sessionDAO;

    public String getForceLogoutUrl() {
        return forceLogoutUrl;
    }

    public void setForceLogoutUrl(String forceLogoutUrl) {
        this.forceLogoutUrl = forceLogoutUrl;
    }

    public SessionDAO getSessionDAO() {
        return sessionDAO;
    }

    public void setSessionDAO(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        if(subject==null||subject.getSession(false)==null){
            return true;
        }
        Session session = sessionDAO.readSession(subject.getSession().getId());
        if(session!=null&&session instanceof OnlineSession){
            OnlineSession onlineSession= (OnlineSession) session;
            servletRequest.setAttribute(ShiroConstants.ONLINE_SESSION,onlineSession);

            if(onlineSession.getStatus()==OnlineSession.OnlineStatus.force_logout){
                return false;
            }
        }
        return true;
    }

    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        if (subject != null) {
            subject.logout();
        }
        saveRequestAndRedirectToLogin(servletRequest, servletResponse);
        return true;
    }


    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        WebUtils.issueRedirect(request, response, getForceLogoutUrl());
    }
}
