package com.youyuan.queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang3.StringUtils;

import javax.jms.*;
import java.io.IOException;

/**
 * @author zhangy
 * @version 1.0
 * @description 测试ActiveMQ消息模型queue的消息消费者
 * @date 2019/12/25 11:48
 */
public class JmsConsumer {
    //定义Linux的ActiveMQ的IP
    private final static String HOST = "192.168.1.18";
    //定义Linux的ActiveMQ的端口号
    private final static int PORT = 61616;
    //定义Linux的ActiveMQ的地址
    private final static String ACTIVEMQ_URL = "tcp://" + HOST + ":" + PORT;
    //定义队列名
    private final static String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException, IOException {
        //1、创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、创建Connection
        Connection connection = connectionFactory.createConnection();
        //3、打开连接ActiveMQ
        connection.start();
        //4、创建Session  第一个参数是否开启事务  第二个参数是否自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、创建队列
        Queue queue = session.createQueue(QUEUE_NAME);
        //6、创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //7、消费者循环接收消息
        //7.1、第一种接收消息的方式 阻塞或设置过期时间
//        while (true) {
//            //TextMessage message = (TextMessage) consumer.receive();//一直阻塞等待
//            TextMessage message = (TextMessage) consumer.receive(1000L);//连接等待10秒，拿不到数据断开连接,如果能拿到数据消费完数据后断开连接
//            if (message != null) {
//                System.out.println("接收到MQ的消息是:" + message.getText());
//            } else {
//                break;
//            }
//        }

        //7.2、使用监听器的方式消费消息  注册一个监听器，是异步非阻塞的方式，当消息到达的时候触发监听事件onMessage进行消费
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //因为生产者发送的消息是TextMessage类型，所以接收者接收的消息也必须是TextMessage
                if (null!=message && message instanceof TextMessage){
                    TextMessage textMessage= (TextMessage) message;
                    try {
                        System.out.println("消费者通过监听器的方式接收到的消息是:"+textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //保证控制台进程不被关闭
        System.in.read();

        //7.3、  一个生产者，启动多个消费者，消费者之间消费消息是平均分发的




        //8、关闭资源信息
        consumer.close();
        session.close();
        connection.close();

        System.out.println("消费者接收消息完成");
    }
}
