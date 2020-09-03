package com.shiro.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.swing.border.EmptyBorder;

public class AnyRolesFilter extends AccessControlFilter {

    private String unauthorizedUrl = "/unauthorized.jsp";
    private String loginUrl = "/login.jsp";

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        String[] roles = (String[]) mappedValue;
        if(roles==null){
            return true;
        }
        for(String role:roles){
            if(getSubject(request,response).hasRole(role)){
            }
        }
        return false;
    }
    

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if(subject.getPrincipal()==null){
           saveRequest(request);
            WebUtils.issueRedirect(request,response,loginUrl);
        }else{
            if(StringUtils.hasText(unauthorizedUrl)){
                WebUtils.issueRedirect(request,response,unauthorizedUrl);
            }else{
                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }

        }
        return false;
    }
}
