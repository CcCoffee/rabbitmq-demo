package com.zhengkehui;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Hello world!
 * @author zhengkehui
 */
public class DirectReceive {

    private static final String EXCHANGE_NAME = "test.direct";

    public static void main( String[] args ) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


        //声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true);

        //声明queue
        String queueName1 = "directQueue1";
        String queueName2 = "directQueue2";
        boolean durable = true;
        boolean exclusive = false;
        boolean autoDelete = false;
        channel.queueDeclare(queueName1,durable,exclusive,autoDelete,null);
        channel.queueDeclare(queueName2,durable,exclusive,autoDelete,null);

        //声明bind
        String routingName1 = "routingName1";
        String routingName2 = "routingName2";
        channel.queueBind(queueName1,EXCHANGE_NAME,routingName1);
        channel.queueBind(queueName2,EXCHANGE_NAME,routingName2);

        DefaultConsumer consumer1 = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println("consumer1："+message);
            }
        };
        channel.basicConsume(queueName1, true, consumer1);

        DefaultConsumer consumer2 = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println("consumer2："+message);
            }
        };
        channel.basicConsume(queueName2, true, consumer2);
    }
}
