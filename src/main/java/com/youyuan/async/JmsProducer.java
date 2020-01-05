package com.youyuan.async;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.AsyncCallback;

import javax.jms.*;
import java.util.UUID;

/**
 * @author zhangy
 * @version 1.0
 * @description 消息异步投递，默认是异步投递，异步投递如何保证消息发送成功，可以设置接收函数来确认消息是否发送成功
 * @date 2020/1/5 7:48
 */
public class JmsProducer {
    //定义服务地址
    private final static String HOST_URL = "tcp://192.168.1.18:61616";
    //定义队列名
    private final static String QUEUE_NAME = "queue_async";

    public static void main(String[] args) throws JMSException {
        //1、创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(HOST_URL);
        //2、设置异步投递模式(默认采用异步投递默认)
        connectionFactory.setUseAsyncSend(true);
        //3、创建Connection
        Connection connection = connectionFactory.createConnection();
        //4、创建Session  第一个参数是否开启事务  第二个参数是否自动签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、创建队列
        Queue queue = session.createQueue(QUEUE_NAME);
        //6、创建ActiveMQMessageProducer
        ActiveMQMessageProducer activeMQMessageProducer = (ActiveMQMessageProducer) session.createProducer(queue);
        TextMessage textMessage = null;
        for (int i = 1; i <= 3; i++) {
            textMessage = session.createTextMessage("测试异步发送消息:" + i);
            textMessage.setJMSMessageID(UUID.randomUUID().toString() + "-youyuan-order");//设置消息头messageid用于回调时确认发送的消息
            String messageId = textMessage.getJMSMessageID();
            //7、发送消息并接收回调
            activeMQMessageProducer.send(textMessage, new AsyncCallback() {
                @Override
                public void onSuccess() {
                    System.out.println(messageId + "  发送成功 ok");
                }

                @Override
                public void onException(JMSException e) {
                    System.out.println(messageId + " 发送失败 fair");
                }
            });
        }

        //8、关闭资源
        activeMQMessageProducer.close();
        session.close();
        connection.close();
    }

}
