package com.youyuan.jdbc.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author zhangy
 * @version 1.0
 * @description 测试ActiveMQ持久化方案到JDBC
 * <p>
 * 消费者
 * 注：持久化方案生产者在生产消息时必须设置成持久化的，如果目的地是queue消费者消费后数据库ACTIVE_MSGS表中的内容会删除，如果目的地是TOPIC的消息会一直存储
 * producer.setDeliveryMode(DeliveryMode.PERSISTENT);
 * @date 2020/1/3 11:44
 */
public class JmsConsumer {
    //定义服务器IP
    private final static String HOST = "192.168.1.18";
    //定义服务器端口号
    private final static int PORT = 61608;//因为ActiveMQ配置文件配置的是auto_nio的协议
    //定义服务器地址
    private final static String HOST_URL = "nio://" + HOST + ":" + PORT;
    //定义队列名
    private final static String QUEUE_NAME = "jdbc_queue";

    public static void main(String[] args) throws JMSException, IOException {
        //1、创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(HOST_URL);
        //2、创建Connection
        Connection connection = connectionFactory.createConnection();
        //3、启动连接
        connection.start();
        //4、创建Session  第一个参数是否开启事务，第二个参数是否自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、创建queue
        Queue queue = session.createQueue(QUEUE_NAME);
        //6、创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //7、消费者注册监听消费消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (null != message && message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("消费者接收到的消息是:" + textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        System.in.read();
        //8、关闭资源
        consumer.close();
        session.close();
        connection.close();
    }
}
