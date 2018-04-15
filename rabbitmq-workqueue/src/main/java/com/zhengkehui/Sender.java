package com.zhengkehui;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * @author zhengkehui
 */
public class Sender {

    private static final String QUEUE_NAME = "helloworld";

    public static void main(String[] args) throws IOException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //设置消息持久化  RabbitMQ不允许使用不同的参数重新定义一个队列，所以已经存在的队列，我们无法修改其属性，
        //需要跟队列原有属性保持一致
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME,durable,false,false,null);

        //发送10条消息，依次在消息后面附加1-10个点
        for (int i = 0; i < 5; i++){
            String dots = "";
            for (int j = 0; j <= i; j++){
                dots += ".";
            }
            String message = "helloworld" + dots+dots.length();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Send '" + message + "'");
        }
        channel.close();
        connection.close();
    }
}
