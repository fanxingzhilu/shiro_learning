package com.shiro.hash;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.BlowfishCipherService;
import org.apache.shiro.crypto.DefaultBlockCipherService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.*;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.Assert;
import org.junit.Test;

import java.security.Key;


public class HashTest {

    @Test
    public void test(){
        String str = "hello";
        String salt = "123";
        String md5 = new Md5Hash(str, salt,2).toString();//还可以转换为 toBase64()/toHex()
        System.out.println(md5);
    }
    @Test
    public void test2(){
        String str = "hello";
        String salt = "123";

        String sha1 = new Sha256Hash(str, salt).toString(); //64位
        //内部使用 MessageDigest String simpleHash = new SimpleHash("SHA-1", str, salt).toString();
        String simpleHash = new SimpleHash("SHA-1", str, salt).toString(); //40位
        System.out.println(sha1);
        System.out.println(sha1.length());
        System.out.println(simpleHash+"======"+simpleHash.length());
    }

    /**
     * base64加解密
     */
    @Test
    public void testBase64(){//64位
        String str="hello";
        String baseEncoded= Base64.encodeToString(str.getBytes());
        String str2=Base64.decodeToString(baseEncoded);
        System.out.println(str+"----"+str2);
        System.out.println(str.equals(str2));
    }

    /**
     * hex加解密
     */
    @Test
    public void testHex() {
        String str = "hello";
        String base64Encoded = Hex.encodeToString(str.getBytes());
        String str2 = new String(Hex.decode(base64Encoded.getBytes()));
        Assert.assertEquals(str, str2);

    }

    /**
     * 用于在 byte 数组/String 之间转换
     */
    @Test
    public void testCodecSupport() {
        String str = "hello";
        byte[] bytes = CodecSupport.toBytes(str, "utf-8");
        String str2 = CodecSupport.toString(bytes, "utf-8");
        Assert.assertEquals(str, str2);
    }

    /**
     * 生成随机数
     */
    @Test
    public void testRandom(){//32位
        SecureRandomNumberGenerator secureRandomNumberGenerator = new SecureRandomNumberGenerator();
        secureRandomNumberGenerator.setSeed("123".getBytes());
        System.out.println(secureRandomNumberGenerator.nextBytes().toHex());
    }
    @Test
    public void testSha256() {//64位
        String str = "hello";
        String salt = "123";
        String sha1 = new Sha256Hash(str, salt).toString();
        System.out.println(sha1.length());

    }

    @Test
    public void testSha384() {//96位
        String str = "hello";
        String salt = "123";
        String sha1 = new Sha384Hash(str, salt).toString();
        System.out.println(sha1.length());

    }

    @Test
    public void testSha512() {//128位
        String str = "hello";
        String salt = "123";
        String sha1 = new Sha512Hash(str, salt).toString();
        System.out.println(sha1.length());

    }

    /**
     * HashService实现
     */
    @Test
    public void testHashService(){
        /*
            1、首先创建一个 DefaultHashService，默认使用 SHA-512 算法；
            2、可以通过 hashAlgorithmName 属性修改算法；
            3、可以通过 privateSalt 设置一个私盐，其在散列时自动与用户传入的公盐混合产生一个新盐；
            4、可以通过 generatePublicSalt 属性在用户没有传入公盐的情况下是否生成公盐；
            5、可以设置 randomNumberGenerator 用于生成公盐；
            6、可以设置 hashIterations 属性来修改默认加密迭代次数；
            7、需要构建一个 HashRequest，传入算法、数据、公盐、迭代次数。
         */
        DefaultHashService defaultHashService = new DefaultHashService();
        defaultHashService.setHashAlgorithmName("SHA-512");//默认
        defaultHashService.setPrivateSalt(new SimpleByteSource("123"));//设置私盐 默认无
        defaultHashService.setGeneratePublicSalt(true); //是否生成公盐 默认false
        defaultHashService.setRandomNumberGenerator(new SecureRandomNumberGenerator());//生成公盐 默认
        defaultHashService.setHashIterations(1);//生成hash值的迭代次数
        HashRequest request = new HashRequest.Builder().setAlgorithmName("MD5").setSource(ByteSource.Util.bytes("hello"))
                .setIterations(2).setSalt(ByteSource.Util.bytes("123")).build();
        String hex = defaultHashService.computeHash(request).toHex();
        System.out.println(hex);
    }
    /**
     * AES实现
     */
    @Test
    public void testAes(){
        AesCipherService aesCipherService = new AesCipherService();
        //设置key的长度
        aesCipherService.setKeySize(128);
        //生成key
        Key key = aesCipherService.generateNewKey();

        String str="hello";
        //加密 64位
        String encryptStr = aesCipherService.encrypt(str.getBytes(), key.getEncoded()).toHex();//转化成16进制
        //解密
        String s = new String(aesCipherService.decrypt(Hex.decode(encryptStr), key.getEncoded()).getBytes());

        System.out.println(encryptStr.length());
        System.out.println(str.equals(s));
    }

    /**
     * Blowfish实现
     */
    @Test
    public void testBlowfishCipherService() {
        BlowfishCipherService blowfishCipherService = new BlowfishCipherService();
        blowfishCipherService.setKeySize(128);

        //生成key
        Key key = blowfishCipherService.generateNewKey();

        String text = "hello";

        //加密 32位
        String encrptText = blowfishCipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();
        //解密
        String text2 = new String(blowfishCipherService.decrypt(Hex.decode(encrptText), key.getEncoded()).getBytes());
        System.out.println(encrptText.length()+"----"+encrptText);
        Assert.assertEquals(text, text2);
    }
    @Test
    public void testDefaultBlockCipherService() throws Exception {

        //对称加密，使用Java的JCA（javax.crypto.Cipher）加密API，常见的如 'AES', 'Blowfish'
        DefaultBlockCipherService cipherService = new DefaultBlockCipherService("AES");
        cipherService.setKeySize(128);

        //生成key
        Key key = cipherService.generateNewKey();

        String text = "hello";

        //加密
        String encrptText = cipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();
        //解密 64位
        String text2 = new String(cipherService.decrypt(Hex.decode(encrptText), key.getEncoded()).getBytes());

        System.out.println(encrptText.length());
        Assert.assertEquals(text, text2);
    }

}
