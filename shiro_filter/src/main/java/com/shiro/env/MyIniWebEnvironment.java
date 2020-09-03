package com.shiro.env;

import org.apache.shiro.util.ClassUtils;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;

import javax.servlet.Filter;

public class MyIniWebEnvironment extends IniWebEnvironment {
    @Override
    protected FilterChainResolver createFilterChainResolver() {
        //在此处扩展自己的FilterChainResolver
        //1、创建FilterChainResolver
        PathMatchingFilterChainResolver filterChainResolver = new PathMatchingFilterChainResolver();
        //2、创建FilterChainManager
        DefaultFilterChainManager filterChainManager = new DefaultFilterChainManager();
        //3、注册Filter
        for(DefaultFilter filter:DefaultFilter.values()){
            filterChainManager.addFilter(filter.name(), (Filter) ClassUtils.newInstance(filter.getFilterClass()));
        }
        //4、注册URL-Filter的映射关系
        filterChainManager.addToChain("/login.jsp","authc");
        filterChainManager.addToChain("/unauthorized.jsp","anon");
        filterChainManager.addToChain("/**","authc");
        filterChainManager.addToChain("/**","roles","admin");
        //5、设置Filter的属性
        FormAuthenticationFilter authc = (FormAuthenticationFilter) filterChainManager.getFilter("authc");
        authc.setLoginUrl("/login.jsp");
        RolesAuthorizationFilter roles = (RolesAuthorizationFilter) filterChainManager.getFilter("roles");
        roles.setUnauthorizedUrl("/unauthorized.jsp");

        filterChainResolver.setFilterChainManager(filterChainManager);

        return filterChainResolver;
    }
}
