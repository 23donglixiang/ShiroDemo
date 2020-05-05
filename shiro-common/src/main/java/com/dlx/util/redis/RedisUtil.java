package com.dlx.util.redis;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author: donglixiang
 * @date: 2020/5/1 12:53
 * @description: Redis工具类
 */
@Component
public class RedisUtil {
    private int expire = 0;

    @Resource
    private JedisPool jedisPool;

    /**
     * @description 判断key是否存在
     * @Param key
     * @return true:存在,false:不存在
     */

    public boolean exist(byte[] key){
        boolean isExist = false;
        Jedis jedis = (Jedis) jedisPool.getResource();
        try{
            isExist = jedis.exists(key);
        } finally {
            jedis.close();
        }
        return isExist;
    }

    /**
     * @description 获取String类型缓存
     * @param key
     * @return value
     */
    public String getString(String key){
        Jedis jedis = jedisPool.getResource();
        String value = null;
        try{
            value = jedis.get(key);
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * @description set String类型缓存
     * @param key value expire:过期时间(S),0为永久有效
     * @return boolean 是否set成功
     */
    public boolean setString(String key,String value,Integer expire){
        Jedis jedis = jedisPool.getResource();
        try{
            jedis.set(key,value);
            if(expire>0 && expire!=0){
                jedis.expire(key,expire);
            }
        } catch (Exception e){
            return false;
        } finally {
            jedis.close();
        }
        return true;
    }
    /**
     * @description 获取redis中的对象实例
     * @param key
     * @return value
     */
    public byte[] getObject(byte[] key) {
        byte[] value = null;
        Jedis jedis = (Jedis)jedisPool.getResource();
        try {
            value = jedis.get(key);
        } finally {
            jedis.close();
        }

        return value;
    }

    public boolean hasKey(){
        Jedis jedis = jedisPool.getResource();
        return false;
    }

    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = (Jedis)jedisPool.getResource();

        try {
            jedis.set(key, value);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        } finally {
            jedis.close();
        }

        return value;
    }

    public byte[] set(byte[] key, byte[] value, int expire) {
        Jedis jedis = (Jedis)jedisPool.getResource();

        try {
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } finally {
            jedis.close();
        }

        return value;
    }

    public void del(byte[] key) {
        Jedis jedis = (Jedis)jedisPool.getResource();

        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }

    }

    public void flushDB() {
        Jedis jedis = (Jedis)jedisPool.getResource();

        try {
            jedis.flushDB();
        } finally {
            jedis.close();
        }

    }

    public Long dbSize() {
        Long dbSize = 0L;
        Jedis jedis = (Jedis)jedisPool.getResource();

        try {
            dbSize = jedis.dbSize();
        } finally {
            jedis.close();
        }

        return dbSize;
    }

    public Set<byte[]> keys(String pattern) {
        Set<byte[]> keys = null;
        Jedis jedis = (Jedis)jedisPool.getResource();

        try {
            keys = jedis.keys(pattern.getBytes());
        } finally {
            jedis.close();
        }

        return keys;
    }

    public int getExpire() {
        return this.expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

}
