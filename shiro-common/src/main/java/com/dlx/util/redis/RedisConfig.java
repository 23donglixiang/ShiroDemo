package com.dlx.util.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author: donglixiang
 * @date: 2020/5/1 12:53
 * @description: redis 配置类
 */
@Configuration
@Slf4j
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWait;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Bean
    public JedisPool redisPoolFactory()  throws Exception{
        log.info("JedisPool开始注入！！");
        log.info("redis地址：" + host + ":" + port);
        JedisPool jedisPool = null;
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMinIdle(minIdle);

        if (this.password != null && !"".equals(this.password)) {
            jedisPool = new JedisPool(new JedisPoolConfig(), this.host, this.port, this.timeout, this.password);
        } else if (this.timeout != 0) {
            jedisPool = new JedisPool(new JedisPoolConfig(), this.host, this.port, this.timeout);
        } else {
            jedisPool = new JedisPool(new JedisPoolConfig(), this.host, this.port);
        }
        log.info("JedisPool注入成功！！");
        return jedisPool;
    }

}
