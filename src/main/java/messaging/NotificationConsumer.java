package messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import config.JsonConfig;
import config.RabbitConfig;
import models.NotificationMessage;

import java.nio.charset.StandardCharsets;

/**
 * Standalone notification system. Runs as a separate process/container and
 * consumes appointment notifications asynchronously from RabbitMQ.
 */
public class NotificationConsumer {
    public static void main(String[] args) throws Exception {
        Connection connection = RabbitConfig.connectionFactory().newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(RabbitConfig.QUEUE, false, false, false, null);

        System.out.println("Notification consumer waiting for messages on queue " + RabbitConfig.QUEUE + "...");

        DeliverCallback callback = (consumerTag, delivery) -> {
            String body = new String(delivery.getBody(), StandardCharsets.UTF_8);
            try {
                NotificationMessage message = JsonConfig.mapper().readValue(body, NotificationMessage.class);
                System.out.println("Notification sent -> " + message.getMessage());
            } catch (Exception exception) {
                System.err.println("Could not process message: " + body);
            }
        };

        channel.basicConsume(RabbitConfig.QUEUE, true, callback, consumerTag -> {
        });
    }
}
