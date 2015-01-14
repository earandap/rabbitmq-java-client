package org.wso2.esb.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Receiver {
    static String queue = "out";
    static String exchange = "out-exchange";
    static String host = "192.168.58.141";
    static String username = "test";
    static String password = "test";
    static int port = 5672;

    public static void main(String[] args) throws IOException,
            ShutdownSignalException, ConsumerCancelledException,
            InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setPort(port);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queue, false, false, false, null);
        channel.exchangeDeclare(exchange, "direct", true);
        channel.queueBind(queue, exchange, "");

        // Create the consumer
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queue, true, consumer);

        // Start consuming messages
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println("Message received");
            System.out.println(message);
        }
    }
} 