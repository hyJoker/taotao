package com.taotao.content.service.test;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class TestJedis {

    @Test
    public void testJedis() {
        //创建jedis对象,需要指定redis服务的ip和端口号
        Jedis jedis = new Jedis("192.168.119.138", 6379);
        //直接操作数据库
        jedis.set("jedis-key", "hello jedis!");
        //获取数据
        String result = jedis.get("jedis-key");
        System.out.println(result);
        //关闭jedis
        jedis.close();
    }

    @Test
    public void testJedisPool() {
        //创建一个数据库连接池对象,单例,即一个系统公用一个连接池,需要制定ip和端口号
        JedisPool jedisPool = new JedisPool("192.168.119.138", 6379);
        //从连接池中获取连接
        Jedis jedis = jedisPool.getResource();
        //使用jedis操作数据库(方法级别,就是说只在该方法中使用,用完就关闭)
        String result = jedis.get("jedis-key");
        System.out.println(result);
        //关闭jedis
        jedis.close();
        //系统关闭前,先关闭数据库
        jedisPool.close();
    }

    //集群测试
    @Test
    public void testJedisCluster() {
        //创建构造参数Set类型，集合中每个元素是HostAndPort类型
        Set<HostAndPort> nodes = new HashSet<>();
        //向集合中添加节点
        nodes.add(new HostAndPort("192.168.156.15", 6379));
        nodes.add(new HostAndPort("192.168.156.16", 6379));
        nodes.add(new HostAndPort("192.168.156.17", 6379));
        nodes.add(new HostAndPort("192.168.156.18", 6379));
        nodes.add(new HostAndPort("192.168.156.19", 6379));
        nodes.add(new HostAndPort("192.168.156.20", 6379));
        //创建JedisCluster对象
        JedisCluster jedisCluster = new JedisCluster(nodes);
        //直接使用jedisCluster，自带连接池，jedisCluster可以是单例的
        jedisCluster.set("jedis-cluster", "hello jedis cluster");
        String result = jedisCluster.get("jedis-cluster");
        System.out.println(result);
        //系统关闭前关闭jedisCluster
        jedisCluster.close();
    }


}
