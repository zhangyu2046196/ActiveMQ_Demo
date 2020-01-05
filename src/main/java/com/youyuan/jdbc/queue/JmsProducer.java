package com.youyuan.jdbc.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author zhangy
 * @version 1.0
 * @description 测试ActiveMQ持久化方案到JDBC
 * <p>
 * 生产者类
 * <p>
 * 注：持久化方案生产者在生产消息时必须设置成持久化的，如果目的地是queue消费者消费后数据库ACTIVE_MSGS表中的内容会删除，如果目的地是TOPIC的消息会一直存储
 * producer.setDeliveryMode(DeliveryMode.PERSISTENT);
 * @date 2020/1/3 11:33
 */
public class JmsProducer {
    //定义服务器IP
    private final static String HOST = "192.168.1.18";
    //定义服务器端口号
    private final static int PORT = 61608;//因为ActiveMQ配置文件配置的是auto_nio的协议
    //定义服务器地址
    private final static String HOST_URL = "nio://" + HOST + ":" + PORT;
    //定义队列名
    private final static String QUEUE_NAME = "jdbc_queue";

    public static void main(String[] args) throws JMSException {
        //1、创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(HOST_URL);
        connectionFactory.setUseAsyncSend(true);  //开启异步投递模式
        //2、创建Connection
        Connection connection = connectionFactory.createConnection();
        //3、启动连接
        connection.start();
        //4、创建Session  第一个参数是否开启事务，第二个参数是否自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、创建queue
        Queue queue = session.createQueue(QUEUE_NAME);
        //6、创建生产者
        MessageProducer producer = session.createProducer(queue);
        //7、设置消息持久化
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        for (int i = 1; i <= 3; i++) {
            //8、创建消息
            TextMessage textMessage = session.createTextMessage("测试持久化jdbc消息" + i);
            //9、发送消息
            producer.send(textMessage);
        }

        //10、关闭资源
        producer.close();
        session.close();
        connection.close();
    }
}
