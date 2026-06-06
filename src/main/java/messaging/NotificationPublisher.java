package messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import config.JsonConfig;
import config.RabbitConfig;
import models.NotificationMessage;

public class NotificationPublisher {
    private Connection connection;
    private Channel channel;

    public NotificationPublisher() {
        connect();
    }

    public void publish(NotificationMessage message) {
        try {
            ensureChannel();
            byte[] body = JsonConfig.mapper().writeValueAsBytes(message);
            channel.basicPublish("", RabbitConfig.QUEUE, null, body);
            System.out.println("Message published to queue " + RabbitConfig.QUEUE
                    + ": appointment " + message.getAppointmentId());
        } catch (Exception exception) {
            // Messaging failures must not break the REST API.
            System.err.println("Could not publish notification: " + exception.getMessage());
        }
    }

    private void ensureChannel() throws Exception {
        if (channel == null || !channel.isOpen()) {
            connect();
        }
        if (channel == null || !channel.isOpen()) {
            throw new IllegalStateException("RabbitMQ channel is not available.");
        }
    }

    private void connect() {
        try {
            connection = RabbitConfig.connectionFactory().newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(RabbitConfig.QUEUE, false, false, false, null);
        } catch (Exception exception) {
            System.err.println("Could not connect to RabbitMQ: " + exception.getMessage());
        }
    }
}
