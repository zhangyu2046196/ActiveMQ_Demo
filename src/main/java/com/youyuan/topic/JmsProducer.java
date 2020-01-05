package com.youyuan.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author zhangy
 * @version 1.0
 * @description 测试消息模型topic发布订阅模式，一条消息可以被多个订阅者消费
 *
 * 注：需要先启动消费者，否则在消费者启动之前生产者发送的消息是收不到
 *
 * @date 2019/12/25 18:42
 */
public class JmsProducer {
    //定义服务端IP
    private final static String HOST = "192.168.1.18";
    //定义服务端端口号
    private final static int PORT = 61616;
    //定义服务端地址
    private final static String ACTIVEMQ_URL = "tcp://" + HOST + ":" + PORT;
    //定义消息模型topic名称
    private final static String TOPIC_NAME = "topic-youyuan";

    public static void main(String[] args) throws JMSException {
        //1、创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、创建Connection
        Connection connection = connectionFactory.createConnection();
        //3、打开连接
        connection.start();
        //4、创建Session  第一个参数是是否开启事务  第二个参数是否自动提交
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、创建消息模型topic
        Topic topic = session.createTopic(TOPIC_NAME);
        //6、创建消息生产者
        MessageProducer producer = session.createProducer(topic);
        for (int i = 1; i <= 3; i++) {
            //7、创建消息内容
            TextMessage message = session.createTextMessage();
            message.setText("这是topic的第" + i + "条消息");
            //8、生产者发送消息
            producer.send(message);
        }
        //9、关闭资源
        producer.close();
        session.close();
        connection.close();
    }

}
