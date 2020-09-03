package com.shiro.util;

import com.shiro.entity.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PasswordHelper {

   private SecureRandomNumberGenerator randomNumberGenerator= new SecureRandomNumberGenerator();

   private String algorithmName="md5";
   private final  int hashIterations=2;

   public void encryptPassword(User user){
       user.setSalt(randomNumberGenerator.nextBytes().toHex());
       String newPassword = new SimpleHash(algorithmName, user.getPassword(), ByteSource.Util.bytes(user.getCredentialsSalt()), hashIterations).toHex();
       user.setPassword(newPassword);
   }
}
