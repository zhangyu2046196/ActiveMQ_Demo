package com.youyuan.topic.persist;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author zhangy
 * @version 1.0
 * @description  测试持久化的topic，先定义订阅者，并且需要注册到服务器上，这样注册后生产者发送的消息不管订阅者是否在线，上线后都能收到
 * @date 2019/12/26 8:57
 */
public class JmsSubscriber {
    //定义服务器IP
    private final static String HOST="192.168.1.18";
    //定义服务器端口号
    private final static int PORT=61616;
    //定义服务器地址
    private final static String ACTIVEMQ_URL="tcp://"+HOST+":"+PORT;
    //定义topic
    private final static String TOPIC_NAME="topic-youyuan01";

    public static void main(String[] args) throws JMSException {
        System.out.println("这是消费者y01");
        //1、创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、创建Connection
        Connection connection = connectionFactory.createConnection();
        //3、设置订阅者的唯一标识
        connection.setClientID("y01");
        //4、创建Session  第一个参数是否开启事务  第二个参数是否自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、创建topic
        Topic topic = session.createTopic(TOPIC_NAME);
        //6、创建subscriber订阅者  第一个参数topic  第二个参数备注信息
        TopicSubscriber subscriber = session.createDurableSubscriber(topic, "remark");
        //7、启动连接
        connection.start();
        //8、订阅者接收消息
        subscriber.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //发送消息的类型是什么，接收消息的类型也必须是什么
                if (null!=message && message instanceof TextMessage){
                    TextMessage textMessage= (TextMessage) message;
                    try {
                        System.out.println("接收到持久化topic的消息："+textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
