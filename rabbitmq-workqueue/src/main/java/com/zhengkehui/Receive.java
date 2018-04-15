package com.zhengkehui;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

/**
 * Hello world!
 * @author zhengkehui
 */
public class Receive {

    private static final String QUEUE_NAME = "helloworld";

    public static void main( String[] args ) throws IOException {

        //区分不同工作进程的输出
        int hashCode = Sender.class.hashCode();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //告诉RabbitMQ同一时间给一个消息给消费者
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        //设置消息持久化  RabbitMQ不允许使用不同的参数重新定义一个队列，所以已经存在的队列，我们无法修改其属性，
        //需要跟队列原有属性保持一致
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME,durable,false,false,null);

        System.out.println(hashCode
                + " [*] Waiting for messages. To exit press CTRL+C");

        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME,autoAck,queueingConsumer);
        while (true){
            try {
                QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
                String message = new String(delivery.getBody());

                System.out.println(hashCode + " [x] Received '" + message + "'");
                doWork(message);
                System.out.println(hashCode + " [x] Done");
                //发送应答
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 每个点耗时1s
     * @param task
     * @throws InterruptedException
     */
    private static void doWork(String task) throws InterruptedException
    {
        for (char ch : task.toCharArray())
        {
            if (ch == '.'){
                Thread.sleep(1000);
            }
        }
    }
}
