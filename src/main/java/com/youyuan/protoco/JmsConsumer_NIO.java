package com.youyuan.protoco;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author zhangy
 * @version 1.0
 * @description 测试使用nio协议作为ActiveMQ的传输协议
 *
 * 消费者
 *
 * @date 2019/12/27 10:33
 */
public class JmsConsumer_NIO {
    //定义服务器地址
    private final static String HOST = "192.168.1.18";
    //定义服务器端口号
    //    private final static int PORT = 61618;//因为nio的协议的端口号是61618
    private final static int PORT = 61608;//使用auto+nio
    //定义服务器地址
    private final static String ACTIVEMQ_URL = "nio://" + HOST + ":" + PORT;
    //定义队列名
    private final static String QUEUE_NAME = "queue_nio";

    public static void main(String[] args) throws JMSException, IOException {
        //1、创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、创建Connection
        Connection connection = connectionFactory.createConnection();
        //3、创建Session  第一个参数是否开启事务，第二个参数是否开启应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4、创建队列
        Queue queue = session.createQueue(QUEUE_NAME);
        //5、创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //6、打开连接
        connection.start();

        //7、定义监听器接收消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (null!=message && message instanceof TextMessage){
                    TextMessage textMessage= (TextMessage) message;
                    try {
                        System.out.println("接收到的消息是:"+textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //使控制台不关闭
        System.in.read();

        //8、关闭资源
        consumer.close();
        session.close();
        connection.close();
    }

}
