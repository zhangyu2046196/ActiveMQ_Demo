package com.youyuan.ack;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author zhangy
 * @version 1.0
 * @description 测试消费者ack和事务之间的关系
 * @date 2019/12/26 9:59
 */
public class JmsConsumer {
    //定义服务器IP
    private final static String HOST = "192.168.1.18";
    //定义服务器端口号
    private final static int PORT = 61616;
    //定义服务器地址
    private final static String ACTIVEMQ_URL = "tcp://" + HOST + ":" + PORT;
    //定义队列名
    private final static String QUEUE_NAME = "queue03";

    public static void main(String[] args) throws JMSException, IOException {
        //1、创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、创建Connection
        Connection connection = connectionFactory.createConnection();
        //3、创建Session  第一个参数是否开启事务，如果开启事务后消费完消息后没有提交事务会导致消息重复消费  第二个参数是应答
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        //4、创建队列
        Queue queue = session.createQueue(QUEUE_NAME);
        //5、创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //6、打开连接
        connection.start();
        //7、接收消息
        TextMessage textMessage = (TextMessage) consumer.receive();
        while (null!=textMessage){
            System.out.println("消费者测试事务接收到的消息是:"+textMessage.getText());
            textMessage.acknowledge();//手动ack
            textMessage= (TextMessage) consumer.receive(5000L);
        }
        //8、提交事务
        //session.commit();
        //9、关闭资源
        consumer.close();
        session.close();
        connection.close();
    }

}
