package com.youyuan.broker;

import org.apache.activemq.broker.BrokerService;

/**
 * @author zhangy
 * @version 1.0
 * @description 嵌入式的ActiveMQ的broker
 * @date 2019/12/26 20:05
 */
public class EmbedBroker {

    public static void main(String[] args) throws Exception {
        //定义broker服务
        BrokerService brokerService = new BrokerService();
        //设置使用jmx
        brokerService.setUseJmx(true);
        //设置连接地址
        brokerService.addConnector("tcp://localhost:61616");
        //启动
        brokerService.start();
    }

}
