package com.shiro.credential;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import java.util.concurrent.atomic.AtomicInteger;

public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
    private Ehcache passwordRetryCache;

    public RetryLimitHashedCredentialsMatcher() {
        CacheManager cacheManager = CacheManager.newInstance(CacheManager.class.getClassLoader().getResource("ehcache.xml"));
         passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username= (String) token.getPrincipal();
        Element element = passwordRetryCache.get(username);
        if(element==null){
           element= new Element(username,new AtomicInteger(0));
           passwordRetryCache.put(element);
        }
        AtomicInteger count = (AtomicInteger) element.getObjectValue();
        if(count.incrementAndGet()>1000){
            throw new ExcessiveAttemptsException();
        }
        boolean match = doCredentialsMatch(token, info);
        if(match){
            passwordRetryCache.remove(username);
        }
        return match;
    }
}
