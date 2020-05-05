package com.dlx.util.redis;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author: donglixiang
 * @date: 2020/5/1 12:53
 * @description: 序列化工具类
 */
@Slf4j
public class SerializerUtil {

    public SerializerUtil() {
    }

    public static Object deserialize(byte[] bytes) {
        Object result = null;
        if (isEmpty(bytes)) {
            return null;
        } else {
            try {
                ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);

                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);

                    try {
                        result = objectInputStream.readObject();
                    } catch (ClassNotFoundException var5) {
                        throw new Exception("Failed to deserialize object type", var5);
                    }
                } catch (Throwable var6) {
                    throw new Exception("Failed to deserialize", var6);
                }
            } catch (Exception var7) {
                log.error("Failed to deserialize", var7);
            }

            return result;
        }
    }

    public static boolean isEmpty(byte[] data) {
        return data == null || data.length == 0;
    }

    public static byte[] serialize(Object object) {
        byte[] result = null;
        if (object == null) {
            return new byte[0];
        } else {
            try {
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream(128);

                try {
                    if (!(object instanceof Serializable)) {
                        throw new IllegalArgumentException(SerializerUtil.class.getSimpleName() + " requires a Serializable payload " + "but received an object of type [" + object.getClass().getName() + "]");
                    }

                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
                    objectOutputStream.writeObject(object);
                    objectOutputStream.flush();
                    result = byteStream.toByteArray();
                } catch (Throwable var4) {
                    throw new Exception("Failed to serialize", var4);
                }
            } catch (Exception var5) {
                log.error("Failed to serialize", var5);
            }

            return result;
        }
    }
}
