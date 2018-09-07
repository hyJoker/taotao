package com.taotao.content.service.test;

import com.taotao.jedis.service.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JedisTestSpring {

    @Test
    public void testJedisClientPool(){
        //初始化spring容器
        ApplicationContext crt=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jedis.xml");
        //从容器中获取jedis对象
        JedisClient jedisClient = crt.getBean(JedisClient.class);
        //测试jedis使用
        jedisClient.set("jedisclient", "test");
        String s = jedisClient.get("jedisclient");
        System.out.println(s);
    }
}
