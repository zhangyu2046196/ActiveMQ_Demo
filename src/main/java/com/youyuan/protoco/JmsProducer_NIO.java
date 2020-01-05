package com.youyuan.protoco;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author zhangy
 * @version 1.0
 * @description 测试使用nio协议作为ActiveMQ的传输协议
 *
 * 生产者
 *
 * @date 2019/12/27 10:33
 */
public class JmsProducer_NIO {
    //定义服务器地址
    private final static String HOST = "192.168.1.18";
    //定义服务器端口号
//    private final static int PORT = 61618;//因为nio的协议的端口号是61618
    private final static int PORT = 61608;//使用auto+nio
    //定义服务器地址
    private final static String ACTIVEMQ_URL = "nio://" + HOST + ":" + PORT;
    //定义队列名
    private final static String QUEUE_NAME = "queue_nio";

    public static void main(String[] args) throws JMSException {
        //1、创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、创建Connection
        Connection connection = connectionFactory.createConnection();
        //3、创建Session  第一个参数是否开启事务，第二个参数是否开启应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4、创建队列
        Queue queue = session.createQueue(QUEUE_NAME);
        //5、创建生产者
        MessageProducer producer = session.createProducer(queue);
        //6、打开连接
        connection.start();

        for (int i = 1; i <= 3; i++) {
            //7、循环发送数据
            producer.send(session.createTextMessage("这是第" + i + "条消息"));
        }

        //8、关闭资源
        producer.close();
        session.close();
        connection.close();
    }

}
