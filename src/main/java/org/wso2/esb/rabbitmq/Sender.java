package org.wso2.esb.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

public class Sender {

    static String queue = "in";
    static String exchange = "in-exchange";
    static String host = "192.168.58.141";
    static String username = "test";
    static String password = "test";
    static int port = 5672;

    public static void main(String[] argv) throws Exception {

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

        // The message to be sent
        String message = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <soapenv:Body>\n" +
                "        <p:greet xmlns:p=\"http://service.earandap.org\">\n" +
                "            <in>IBM</in>\n" +
                "        </p:greet>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        // Populate the AMQP message properties
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties()
                .builder();
        builder.messageId("007");
        builder.contentType("text/xml");
        builder.contentEncoding("UTF-8");

        // Custom user properties
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("SOAP_ACTION", "getQuote");
        builder.headers(headers);

        // Publish the message to exchange
        channel.basicPublish(exchange, queue, builder.build(),message.getBytes());
    }
} 