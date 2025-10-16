package com.bancobase.payments.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for RabbitMQ components.
 * <p>
 * Defines the exchange, queue, binding, and message converter
 * used for payment message processing.
 */
@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    @Value("${rabbitmq.queue}")
    private String queueName;

    /**
     * Creates a topic exchange for payment messages.
     *
     * @return the configured TopicExchange instance
     */
    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(exchangeName);
    }

    /**
     * Creates a durable queue for payment messages.
     *
     * @return the configured Queue instance
     */
    @Bean
    public Queue paymentQueue() {
        return new Queue(queueName, true);
    }

    /**
     * Defines the binding between the queue and exchange
     * using the configured routing key.
     *
     * @param queue the queue to bind
     * @param exchange the exchange to bind to
     * @return the configured Binding instance
     */
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    /**
     * Configures a JSON message converter for RabbitMQ.
     *
     * @return the Jackson-based message converter
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
