package com.zhengkehui;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhengkehui
 */
public class TopicSender {

    private static final String EXCHANGE_NAME = "test.topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"topic",true);
        channel.basicPublish(EXCHANGE_NAME,"book.java.spring",null,"hello spring".getBytes());
        channel.close();
        connection.close();
    }
}
