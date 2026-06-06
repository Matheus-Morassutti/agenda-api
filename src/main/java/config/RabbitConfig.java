package config;

import com.rabbitmq.client.ConnectionFactory;

public final class RabbitConfig {
    public static final String QUEUE = "agenda.notifications";

    private RabbitConfig() {
    }

    public static ConnectionFactory connectionFactory() {
        String host = System.getenv().getOrDefault("RABBIT_HOST", "localhost");
        int port = Integer.parseInt(System.getenv().getOrDefault("RABBIT_PORT", "5672"));

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        return factory;
    }
}
