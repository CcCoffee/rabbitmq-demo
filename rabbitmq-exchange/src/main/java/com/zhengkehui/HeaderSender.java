package com.zhengkehui;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Author zhengkehui
 * @Date 2018/4/16 11:16
 * @Description
 */
public class HeaderSender {

    private static final String EXCHANGE_NAME = "test.headers";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"headers",true);
        Map<String, Object> headers = new HashMap<>();
        headers.put("country","China");
        //headers.put("city","Shantou");
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.headers(headers);
        channel.basicPublish(EXCHANGE_NAME,"",builder.build(),"你好世界".getBytes());
        channel.close();
        connection.close();
    }
}
