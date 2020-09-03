package com.shiro.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FormLoginFilter extends PathMatchingFilter {

    private String loginUrl="/login.jsp";
    private String successUrl="/";

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        if(SecurityUtils.getSubject().isAuthenticated()){
            return true;
        }
        HttpServletRequest req= (HttpServletRequest) request;
        HttpServletResponse resp= (HttpServletResponse) response;
        if(isLoginRequest(req)){
            if("post".equalsIgnoreCase(((HttpServletRequest) request).getMethod())){
                boolean login = login(req);
                if(login){
                    return redirectToSuccessUrl(req,resp);
                }
            }
            return true;
        } else {//保存当前地址并重定向到登录界面
            saveRequestAndRedirectToLogin(req, resp);
            return false;
        }
    }

    private boolean redirectToSuccessUrl(HttpServletRequest request,HttpServletResponse response) throws IOException {
        WebUtils.redirectToSavedRequest(request,response,successUrl);
        return false;
    }

    public void saveRequestAndRedirectToLogin(HttpServletRequest request,HttpServletResponse response) throws IOException {
        WebUtils.saveRequest(request);
        WebUtils.issueRedirect(request,response,loginUrl);
    }

    private boolean login(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username,password));
        } catch (AuthenticationException e) {
            request.setAttribute("shiroLoginFailure", e.getClass());
            return false;
        }
        return true;
    }

    private boolean isLoginRequest(HttpServletRequest request){
        return pathsMatch(loginUrl, WebUtils.getPathWithinApplication(request));
    }
}
