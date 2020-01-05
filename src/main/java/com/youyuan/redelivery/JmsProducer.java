package com.youyuan.redelivery;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;

import javax.jms.*;

/**
 * @author zhangy
 * @version 1.0
 * @description  broker重发消息到consumer测试，代码需要在consumer完成
 * @date 2020/1/5 9:32
 */
public class JmsProducer {
    //定义服务器地址
    private final static String HOST_URL = "tcp://192.168.1.18:61616";
    //定义队列名
    private final static String QUEUE_NAME = "redelivery-queue";

    public static void main(String[] args) throws JMSException {
        //1、创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(HOST_URL);
        //2、创建Connection
        Connection connection = connectionFactory.createConnection();
        //3、开启连接
        connection.start();
        //4、创建Session  第一个参数是否开启事务  第二个参数是否自动签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、创建队列
        Queue queue = session.createQueue(QUEUE_NAME);
        //6、创建生产者
        MessageProducer producer = session.createProducer(queue);
        //7、消息持久化
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        TextMessage textMessage = null;

        for (int i = 1; i <= 3; i++) {
            textMessage = session.createTextMessage("生产者生产消息" + i);
            //8、发送消息
            producer.send(textMessage);
        }

        //9、关闭资源
        producer.close();
        session.close();
        connection.close();
    }
}
