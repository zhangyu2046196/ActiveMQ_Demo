package com.youyuan.schedule;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;

import javax.jms.*;

/**
 * @author zhangy
 * @version 1.0
 * @description 测试延迟投递和定时投递
 * <p>
 * 生产者
 * <p>
 * 四个参数：
 * 1：AMQ_SCHEDULED_DELAY ：延迟投递的时间
 * 2：AMQ_SCHEDULED_PERIOD ：重复投递的时间间隔
 * 3：AMQ_SCHEDULED_REPEAT：重复投递次数
 * 4：AMQ_SCHEDULED_CRON：Cron表达式
 * @date 2020/1/5 8:32
 */
public class JmsProducer {
    //定义服务器地址
    private final static String HOST_URL = "tcp://192.168.1.18:61616";
    //定义队列名
    private final static String QUEUE_NAME = "schedule-queue";

    public static void main(String[] args) throws JMSException {
        //1、创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(HOST_URL);
        //2、创建Connection
        Connection connection = connectionFactory.createConnection();
        //3、开启连接
        connection.start();
        //4、创建Session  第一个参数是否开启事务  第二个参数是否自动签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、创建队列
        Queue queue = session.createQueue(QUEUE_NAME);
        //6、创建生产者
        MessageProducer producer = session.createProducer(queue);
        //7、消息持久化
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        //8、设置延迟投递和定时投递参数
        long delay = 3 * 1000;  //延迟投递的时间3秒
        long period = 4 * 1000; //定时重复投递时间间隔4秒
        int repeat = 5;   //重复投递次数5次

        TextMessage textMessage = null;

        for (int i = 1; i <= 3; i++) {
            textMessage = session.createTextMessage("测试延迟投递和定时投递" + i);
            //9、设置消息的延迟投递和定时投递属性
            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, period);
            textMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, repeat);
            //10、发送消息
            producer.send(textMessage);
        }

        //11、关闭资源
        producer.close();
        session.close();
        connection.close();
    }

}
