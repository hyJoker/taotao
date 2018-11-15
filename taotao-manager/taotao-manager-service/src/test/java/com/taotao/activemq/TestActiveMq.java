package com.taotao.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;

public class TestActiveMq {

    //发送queue消息
    @Test
    public void testQueueProducer() throws JMSException {
        //创建一个连接工厂,需要指定mq服务ip和端口号,注意:连接方式是是tcp,端口是61616
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.119.138:61616");
        //使用connectionFactor创建一个connection对象
        Connection connection = connectionFactory.createConnection();
        //开启连接,调用start对象
        connection.start();
        //使用connection对象创建一个session对象
        //第一个参数是是否开启事务,一般不使用分布式事务,因为他特别消耗性能,而且顾客体验特别差,如果第一个参数为true,第二个参数将会
        //被忽略,如果第一个参数为false,第二个参数为消息的应答模式,常见的有手动和自动两种,一般使用自动
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建一个队列
        Queue queue = session.createQueue("test-queue");
        //创建一个procedure对象
        MessageProducer producer = session.createProducer(queue);
        //创建一个textMessage
        TextMessage textMessage=new ActiveMQTextMessage();
        textMessage.setText("hello queue3");
        //发送消息
        producer.send(textMessage);
        //关闭资源
        producer.close();
        session.close();
        connection.close();
    }

    //消费queue消息
    @Test
    public void testQueueConsumer() throws JMSException, IOException {
        //创建一个连接工厂,指定mq的ip和端口号
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.119.138:61616");
        //创建一个连接
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //创建一个session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("test-queue");
        MessageConsumer consumer = session.createConsumer(queue);
        //向consumer中设置MessageListener,接收消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage){
                    TextMessage textMessage= (TextMessage) message;
                    try {
                        String text = textMessage.toString();
                        System.out.println(text);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        //程序等待接收用户的结束操作
        //程序不知道什么时候有消息,也不知道什么不在发消息,这就需要手动干预,
        //当我们需要停止的时候,在控制台输入信息,回车即可
        System.in.read();
        consumer.close();
        session.close();
        connection.close();
    }

    //发送topic消息
    @Test
    public void testTopicProducer() throws JMSException {
        //创建一个连接工厂,需要指定mq服务ip和端口号,注意:连接方式是是tcp,端口是61616
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.119.138:61616");
        //使用connectionFactor创建一个connection对象
        Connection connection = connectionFactory.createConnection();
        //开启连接,调用start对象
        connection.start();
        //使用connection对象创建一个session对象
        //第一个参数是是否开启事务,一般不使用分布式事务,因为他特别消耗性能,而且顾客体验特别差,如果第一个参数为true,第二个参数将会
        //被忽略,如果第一个参数为false,第二个参数为消息的应答模式,常见的有手动和自动两种,一般使用自动
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建一个队列
        Topic topic = session.createTopic("test-topic");
        //创建一个procedure对象
        MessageProducer producer = session.createProducer(topic);
        //创建一个textMessage
        TextMessage textMessage=new ActiveMQTextMessage();
        textMessage.setText("hello topic1");
        //发送消息
        producer.send(textMessage);
        //关闭资源
        producer.close();
        session.close();
        connection.close();
    }

    //接收topic消息
    @Test
    public void testTopicConsumer() throws JMSException, IOException {
        //创建一个连接工厂,指定mq的ip和端口号
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.119.138:61616");
        //创建一个连接
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //创建一个session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("test-topic");
        MessageConsumer consumer = session.createConsumer(topic);
        //向consumer中设置MessageListener,接收消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage){
                    TextMessage textMessage= (TextMessage) message;
                    try {
                        String text = textMessage.toString();
                        System.out.println(text);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        //程序等待接收用户的结束操作
        //程序不知道什么时候有消息,也不知道什么不在发消息,这就需要手动干预,
        //当我们需要停止的时候,在控制台输入信息,回车即可
        System.out.println("消费者1----");
        System.in.read();
        consumer.close();
        session.close();
        connection.close();
    }


    //发送topic消息(持久化)
    @Test
    public void testTopicPersistenceProducer() throws JMSException {
        //创建一个连接工厂,需要指定mq服务ip和端口号,注意:连接方式是是tcp,端口是61616
        ActiveMQConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.119.138:61616");
        //设置使用异步发送消息
        connectionFactory.setUseAsyncSend(true);
        //使用connectionFactor创建一个connection对象
        Connection connection = connectionFactory.createConnection();
        //设置clientId,必须唯一
        connection.setClientID("producer1");
        //开启连接,调用start对象
        connection.start();
        //使用connection对象创建一个session对象
        //第一个参数是是否开启事务,一般不使用分布式事务,因为他特别消耗性能,而且顾客体验特别差,如果第一个参数为true,第二个参数将会
        //被忽略,如果第一个参数为false,第二个参数为消息的应答模式,常见的有手动和自动两种,一般使用自动
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建一个队列
        Topic topic = session.createTopic("test-topic");
        //创建一个procedure对象
        MessageProducer producer = session.createProducer(topic);
        //设置持久化
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        //创建一个textMessage
        TextMessage textMessage=new ActiveMQTextMessage();
        textMessage.setText("hello topic5");
        //发送消息
        producer.send(textMessage);
        //关闭资源
        producer.close();
        session.close();
        connection.close();
    }

    //接收topic消息(持久化)
    @Test
    public void testTopicPersistenceConsumer() throws JMSException, IOException {
        //创建一个连接工厂,指定mq的ip和端口号
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.119.138:61616");
        //设置异步接收消息
        connectionFactory.setUseAsyncSend(true);
        //创建一个连接
        Connection connection = connectionFactory.createConnection();
        //设置消费者id,必须唯一
        connection.setClientID("consumer1");
        //开启连接
        connection.start();
        //创建一个session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("test-topic");
        //MessageConsumer consumer = session.createConsumer(topic);
        TopicSubscriber consumer = session.createDurableSubscriber(topic, "consumer1");
        //向consumer中设置MessageListener,接收消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage){
                    TextMessage textMessage= (TextMessage) message;
                    try {
                        String text = textMessage.toString();
                        System.out.println(text);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        //程序等待接收用户的结束操作
        //程序不知道什么时候有消息,也不知道什么不在发消息,这就需要手动干预,
        //当我们需要停止的时候,在控制台输入信息,回车即可
        System.out.println("消费者1----");
        System.in.read();
        consumer.close();
        session.close();
        connection.close();
    }
}
