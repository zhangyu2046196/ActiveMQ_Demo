package com.youyuan.cluster;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author zhangy
 * @version 1.0
 * @description zookeeper+replicad leveldb高可用集群测试
 * <p>
 * 消费者
 * @date 2020/1/4 14:50
 */
public class JmsConsumer {
    //定义集群服务地址
    private final static String HOST_URL = "failover:(tcp://192.168.1.18:61616,tcp://192.168.1.19:61616,tcp://192.168.1.20:61616)?randomize=false";
    //定义队列名
    private final static String QUEUE_NAME = "cluster_queue";

    public static void main(String[] args) throws JMSException, IOException {
        //1、创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(HOST_URL);
        //2、创建Connection
        Connection connection = connectionFactory.createConnection();
        //3、启动连接
        connection.start();
        //4、创建Session  第一个参数是否开启事务  第二个参数是否自动签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、创建队列
        Queue queue = session.createQueue(QUEUE_NAME);
        //6、创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //7、消费者接收消息
        while (true) {
            TextMessage textMessage = (TextMessage) consumer.receive(10000L);
            if (null == textMessage) {
                break;
            } else {
                System.out.println("消费者接收到集群消息是:" + textMessage.getText());
            }
        }
        //8、关闭资源
        consumer.close();
        session.close();
        connection.close();

    }

}
