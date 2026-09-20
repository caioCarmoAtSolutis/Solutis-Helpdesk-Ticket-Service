package com.solutis.helpdesk.service.ticket.message.rabbitmq.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketServiceAMQPConfiguration {
    @Value("${rabbitmq.ticket.created.routing-key}")
    private String ticketCreatedRoutingkey;

    @Value("${rabbitmq.ticket.assigned.routing-key}")
    private String ticketAssignedRoutingkey;

    @Value("${rabbitmq.ticket.status-changed.routing-key}")
    private String ticketStatusChangedRoutingkey;

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initializeRabbitAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public Queue createTicketCreatedQueue() {
        return QueueBuilder.durable(ticketCreatedRoutingkey).build();
    }

    @Bean
    public Queue createTicketAssignedQueue() {
        return QueueBuilder.durable(ticketAssignedRoutingkey).build();
    }

    @Bean
    public Queue createTicketStatusChangedQueue() {
        return QueueBuilder.durable(ticketStatusChangedRoutingkey).build();
    }
}
