package com.shiro.authenticator;

import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import sun.security.krb5.Realm;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class MyAuthenticator extends ModularRealmAuthenticator {

    //byte数组注入 setter
    public void setBytes(byte[] bytes){
        System.out.println(new String(bytes));
    }
    //
    public void setArray(int[] ints){
        System.out.println(Arrays.toString(ints));
    }
    public void setSet(Set<Realm> realms){
        System.out.println(realms);
    }
    public void setMap(Map<Object,Object> maps){
        System.out.println(maps);
        System.out.println(maps.get("1"));
    }
}
