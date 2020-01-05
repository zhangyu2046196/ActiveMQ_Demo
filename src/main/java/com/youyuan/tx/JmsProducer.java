package com.youyuan.tx;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author zhangy
 * @version 1.0
 * @description 测试生产者发送消息的事务
 * @date 2019/12/26 9:46
 */
public class JmsProducer {
    //定义服务器IP
    private final static String HOST = "192.168.1.18";
    //定义服务器端口号
    private final static int PORT = 61616;
    //定义服务端地址
    private final static String ACTIVEMQ_URL = "tcp://" + HOST + ":" + PORT;
    //定义队列名
    private final static String QUEUE_NAME = "queue02";

    public static void main(String[] args) throws JMSException {
        //1、定义ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、定义Connection
        Connection connection = connectionFactory.createConnection();
        //3、定义Session  第一个参数是否开启事务，如果不开启事务发的消息直接到队列或topic，如果开启事务必须commit提交事务后发送的消息才能进入队列或topic
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        //4、创建队列
        Queue queue = session.createQueue(QUEUE_NAME);
        //5、创建生产者
        MessageProducer producer = session.createProducer(queue);
        //6、打开连接
        connection.start();
        for (int i = 1; i <= 3; i++) {
            //7、循环发送消息
            producer.send(session.createTextMessage("测试生产者发送消息的事务" + i));
        }
        //8、提交事务
        session.commit();
        //9、关闭资源
        producer.close();
        session.close();
        connection.close();
    }

}
