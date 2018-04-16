package com.zhengkehui;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * @author zhengkehui
 */
public class DirectSender {

    private static final String EXCHANGE_NAME = "test.direct";
    private static final String[] ROUTING_KEYS = {"routingName1","routingName2"};
    private static final String[] MESSAGES = {"hello,consumer1","hello,consumer2"};

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true);
        for (int i = 0;i<10;i++){
            int idx = new Random().nextInt(2);
            //只有消费者的ROUTING_KEY跟生产者发出的ROUTING_KEY相同时才能消费到消息
            channel.basicPublish(EXCHANGE_NAME,ROUTING_KEYS[idx],null,MESSAGES[idx].getBytes());
        }
        channel.close();
        connection.close();
    }
}
