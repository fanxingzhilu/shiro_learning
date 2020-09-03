package org.apache.shiro.util;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.Session;

import java.io.*;

public class SerializableUtils {
    /**
     * 序列化 存到数据库
     * @param session
     * @return
     */
    public static String serialize(Session session){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(session);
            return Base64.encodeToString(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("serialize session error", e);
        }
    }

    /**
     * 反序列化 从数据库读
     * @param sessionStr
     * @return
     */
    public static Session deserialize(String sessionStr){
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decode(sessionStr));
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (Session ) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("deserialize session error", e);
        }
    }
}
