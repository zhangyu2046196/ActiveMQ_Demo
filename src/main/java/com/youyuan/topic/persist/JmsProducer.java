package com.youyuan.topic.persist;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author zhangy
 * @version 1.0
 * @description 测试持久化的topic，当订阅者topicSubscribe注册到topic后关机，开机后还能收到生产者发送的消息
 * @date 2019/12/26 8:46
 */
public class JmsProducer {
    //定义服务器IP
    private final static String HOST = "192.168.1.18";
    //定义服务器端口号
    private final static int PORT = 61616;
    //定义服务器地址
    private final static String ACTIVEMQ_URL = "tcp://" + HOST + ":" + PORT;
    //定义topic名称
    private final static String TOPIC_NAME = "topic-youyuan01";

    public static void main(String[] args) throws JMSException {
        //1、创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、创建Connection
        Connection connection = connectionFactory.createConnection();
        //3、创建Session  第一个参数是否开启事务  第二个参数是否自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4、创建topic
        Topic topic = session.createTopic(TOPIC_NAME);
        //5、创建生产者
        MessageProducer producer = session.createProducer(topic);
        //6、打开连接
        connection.start();
        for (int i = 1; i <= 3; i++) {
            //7、循环发送数据
            TextMessage textMessage = session.createTextMessage("这是持久化topic-youyuan01的topic的" + i + "记录");
            producer.send(textMessage);
        }
        //8、关闭资源
        producer.close();
        session.close();
        connection.close();

        System.out.println("生产者发送消息到持久化topic的MQ");
    }
}
