package com.youyuan.schedule;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author zhangy
 * @version 1.0
 * @description 测试延迟投递和定时投递
 * <p>
 * 消费者
 * <p>
 * 四个参数：
 * 1：AMQ_SCHEDULED_DELAY ：延迟投递的时间
 * 2：AMQ_SCHEDULED_PERIOD ：重复投递的时间间隔
 * 3：AMQ_SCHEDULED_REPEAT：重复投递次数
 * 4：AMQ_SCHEDULED_CRON：Cron表达式
 * @date 2020/1/5 8:46
 */
public class JmsConsumer {
    //定义服务器地址
    private final static String HOST_URL = "tcp://192.168.1.18:61616";
    //定义队列名
    private final static String QUEUE_NAME = "schedule-queue";

    public static void main(String[] args) throws JMSException, IOException {
        //1、创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(HOST_URL);
        //2、创建Connection
        Connection connection = connectionFactory.createConnection();
        //3、打开连接
        connection.start();
        //4、创建Session  第一个参数是否开启事务  第二个参数是否开启签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、创建队列
        Queue queue = session.createQueue(QUEUE_NAME);
        //6、创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //7、取出消息
//        while (true) {
//            TextMessage textMessage = (TextMessage) consumer.receive(10L);
//            if (null != textMessage) {
//                System.out.println("客户端接收到的消息是:" + textMessage.getText());
//            } else {
//                break;
//            }
//        }

        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (null!=message && message instanceof TextMessage){
                    TextMessage textMessage= (TextMessage) message;
                    try {
                        System.out.println("客户端接收到的消息是:" + textMessage.getText());
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
