package com.youyuan.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.awt.*;

/**
 * @author zhangy
 * @version 1.0
 * @description 测试消息两种模型queue的生产者
 * @date 2019/12/25 11:15
 */
public class JmsProducer {
    //定义Linux上的ActiveMQ的地址
    private final static String HOST = "192.168.1.18";
    //定义Linux上的ActiveMQ的端口
    private final static int PORT = 61616;
    //拼装Linux上的ActiveMQ的访问地址
    private final static String ACTIVEMQ_URL = "tcp://" + HOST + ":" + PORT;
    //定义队列名
    private final static String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException {
        //1、创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、创建connection连接
        Connection connection = connectionFactory.createConnection();
        //3、打开连接
        connection.start();
        //4、创建session  第一个参数指是否开启事物   第二个参数指是否自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、创建队列
        Queue queue = session.createQueue(QUEUE_NAME);
        //6、创建消息生产者
        MessageProducer producer = session.createProducer(queue);
        for (int i = 1; i <= 3; i++) {
            //7、循环创建三条消息
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText("这是activemq的第" + i + "条消息");
            //8、消息生产者发送消息到队列
            producer.send(textMessage);
        }
        //9、关闭资源连接
        producer.close();
        session.close();
        connection.close();

        System.out.println("消息发送ActiveMQ成功");
    }
}
