package com.youyuan.cluster;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author zhangy
 * @version 1.0
 * @description zookeeper+replicad leveldb高可用集群测试
 * <p>
 * 生产者
 * @date 2020/1/4 14:50
 */
public class JmsProducer {
    //定义集群服务地址
    private final static String HOST_URL = "failover:(tcp://192.168.1.18:61616,tcp://192.168.1.19:61616,tcp://192.168.1.20:61616)?randomize=false";
    //定义队列名
    private final static String QUEUE_NAME = "cluster_queue";

    public static void main(String[] args) throws JMSException {
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
        //6、创建生产者
        MessageProducer producer = session.createProducer(queue);
        //7、生产者设置消息持久化
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        for (int i = 1; i <= 3; i++) {
            TextMessage textMessage = session.createTextMessage("高可用集群发送消息" + i);
            //8、生产者发送消息
            producer.send(textMessage);
        }

        //9、关闭资源
        producer.close();
        session.close();
        connection.close();

    }

}
