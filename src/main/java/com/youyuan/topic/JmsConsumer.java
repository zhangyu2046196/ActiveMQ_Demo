package com.youyuan.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author zhangy
 * @version 1.0
 * @description 测试消息模型topic  生产者发送一条消息 多个消费者都可以接收到
 * <p>
 * 注：需要先启动消费者，否则在消费者启动之前生产者发送的消息收不到  可以执行多次模拟多个消费者
 * @date 2019/12/25 18:51
 */
public class JmsConsumer {
    //定义服务端IP
    private static final String HOST = "192.168.1.18";
    //定义服务端端口号
    private static final int PORT = 61616;
    //定义服务端地址
    private final static String ACTIVEMQ_URL = "tcp://" + HOST + ":" + PORT;
    //定义topic名称
    private static final String TOPIC_NAME = "topic-youyuan";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("这是3号消费者");
        //1、创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、创建Connection
        Connection connection = connectionFactory.createConnection();
        //3、打开连接
        connection.start();
        //4、创建Session  第一个参数是否开启事务  第二个参数是否自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、创建topic
        Topic topic = session.createTopic(TOPIC_NAME);
        //6、创建消费者
        MessageConsumer consumer = session.createConsumer(topic);
        //7、创建监听器的方式监听消息的发送
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //因为生产者发送消息类型是TextMessage所以消费者接收消息的类型也指定TextMessage
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

        System.in.read();//阻塞防止消费进程关闭

        //8、关闭资源
        consumer.close();
        session.close();
        connection.close();
    }

}
