package com.shiro.hash;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.junit.Test;

public class PasswordServiceTest extends BaseTest {

    /**
     * 测试passwordService 自定义realm
     */
    @Test
    public void testPasswordServiceWithMyRealm() {
        login("classpath:shiro-passwordservice.ini", "wu", "123");
    }

    /**
     * 测试passwordService jdbcRealm
     */
    @Test
    public void testPasswordServiceWithJdbcRealm() {
        login("classpath:shiro-jdbc-passwordservice.ini", "wu", "123");
    }

    /**
     * 生成密码散列值
     */
    @Test
    public void testGeneratePassword() {
        String algorithmName = "md5";
        String username = "liu";
        String password = "234";
        String salt1 = username;
        String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();
        int hashIterations = 2;

        SimpleHash hash = new SimpleHash(algorithmName, password, salt1 + salt2, hashIterations);
        String encodedPassword = hash.toHex();
        System.out.println(salt2);
        System.out.println(encodedPassword);
    }

    /**
     * 测试hashedCredentialMatcher 自定义realm
     */
    @Test
    public void testHashedCredentialsMatcherWithMyRealm2() {
        //使用testGeneratePassword生成的散列密码
        login("classpath:shiro-hashedCredentialsMatcher.ini", "liu", "123");
    }

    /**
     * 测试hashedCredentialMatcher jdbcRealm
     */
    @Test
    public void testHashedCredentialsMatcherWithJdbcRealm() {

     BeanUtilsBean.getInstance().getConvertUtils().register(new EnumConverter(),JdbcRealm.SaltStyle.class);
        //使用testGeneratePassword生成的散列密码
        login("classpath:shiro-jdbc-hashedCredentialsMatcher.ini", "liu", "123");
    }

    /**
     * 需要一个转化器
     */
    private class EnumConverter extends AbstractConverter{
        @Override
        protected String convertToString(final Object value) throws Throwable {
           return ((Enum)value).name();
        }

        @Override
        protected Object convertToType(Class type, Object value) throws Throwable {
            return Enum.valueOf(type,value.toString());
        }
        @Override
        protected Class getDefaultType() {
            return null;
        }
    }

    /**
     * 测试密码重试次数限制
     */
    @Test
    public void testRetryLimitHashedCredentialsMatcherWithMyRealm() {
        for(int i = 1; i <= 5; i++) {
            try {
                login("classpath:shiro-retryLimitHashedCredentialsMatcher.ini", "liu", "234");
            } catch (Exception e) {/*ignore*/}
        }
        login("classpath:shiro-retryLimitHashedCredentialsMatcher.ini", "liu", "234");
    }
}
