package com.taotao.jedis.service.impl;

import com.taotao.jedis.service.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.logging.FileHandler;

//单机版jedis客户端
@Service
public class JedisClientPool implements JedisClient {

    @Autowired
    private JedisPool jedisPool;

    @Override
    public String set(String key, String value) {
        Jedis jedis = getJedis();
        String result = jedis.set(key, value);
        close(jedis);
        return result;
    }

    private void close(Jedis jedis) {
        jedis.close();
    }

    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    @Override
    public String get(String key) {
        Jedis jedis = getJedis();
        String result = jedis.get(key);
        close(jedis);
        return result;
    }

    @Override
    public Boolean exists(String key) {
        Jedis jedis = getJedis();
        Boolean exists = jedis.exists(key);
        close(jedis);
        return exists;
    }

    @Override
    public Long expire(String key, int seconds) {
        Jedis jedis = getJedis();
        Long expire = jedis.expire(key, seconds);
        close(jedis);
        return expire;
    }

    @Override
    public Long ttl(String key) {
        Jedis jedis = getJedis();
        Long ttl = jedis.ttl(key);
        close(jedis);
        return ttl;
    }

    @Override
    public Long incr(String key) {
        Jedis jedis = getJedis();
        Long incr = jedis.incr(key);
        close(jedis);
        return incr;
    }

    @Override
    public Long hset(String key, String field, String value) {
        Jedis jedis = getJedis();
        Long hset = jedis.hset(key, field, value);
        close(jedis);
        return hset;
    }

    @Override
    public String hget(String key, String field) {
        Jedis jedis = getJedis();
        String hget = jedis.hget(key, field);
        close(jedis);
        return hget;
    }

    @Override
    public Long hdel(String key, String... field) {
        Jedis jedis = getJedis();
        Long hdel = jedis.hdel(key, field);
        close(jedis);
        return hdel;
    }
}
