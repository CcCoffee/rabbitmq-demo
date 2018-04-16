package com.zhengkehui;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Author zhengkehui
 * @Date 2018/4/16 11:24
 * @Description
 */
public class HeaderReceive {

    private static final String EXCHANGE_NAME = "test.headers";
    private static final String QUEUE_NAME = "directQueue1";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME,"headers",true);
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-match","all");
        arguments.put("country","China");
        arguments.put("city","Shantou");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "", arguments);
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "utf-8");
                System.out.println(message);
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
